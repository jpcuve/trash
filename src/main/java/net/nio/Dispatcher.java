package net.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 19, 2004
 * Time: 2:05:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dispatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);
    private long pulse;
    private Thread thread = null;
    private Selector selector = null;
    private boolean kill = false;
    private final List<Runnable> laters = new ArrayList<Runnable>();

    public Dispatcher(long pulse) throws IOException {
        this.pulse = pulse;
        this.selector = Selector.open();
    }

    public Thread getThread() {
        return thread;
    }

    public synchronized void kill(){
        LOGGER.debug("killing dispatcher thread");
        kill = true;
        selector.wakeup();
    }

    public boolean isKill() {
        return kill;
    }

    public void invokeLater(final Runnable runnable){
        synchronized(laters){
            laters.add(runnable);
        }
        selector.wakeup();
    }

    public void registerChannelNow(final SelectableChannel selectableChannel, final int interest, final SelectorHandler selectorHandler) throws IOException {
        if (Thread.currentThread() != thread) throw new IOException("method called by invalid thread");
        if (!selectableChannel.isOpen()) throw new IOException("selectable channel not init");
        if (selectableChannel.isRegistered()){
            SelectionKey selectionKey = selectableChannel.keyFor(selector);
            assert selectionKey != null: "selectable channel already registered with another selector";
            selectionKey.interestOps(interest);
            Object previousAttach = selectionKey.attach(selectorHandler);
            assert previousAttach != null;
        } else{
            selectableChannel.configureBlocking(false);
            selectableChannel.register(selector, interest, selectorHandler);
        }
    }

    public void setChannelInterestNow(final SelectableChannel selectableChannel, final int interest, final boolean enable) throws IOException {
        if (Thread.currentThread() != thread) throw new IOException("method called by invalid thread");
        SelectionKey selectionKey = selectableChannel.keyFor(selector);
        if (selectionKey == null) throw new IOException("channel not registered with selector");
        if (enable) selectionKey.interestOps(selectionKey.interestOps() | interest);
        else selectionKey.interestOps(selectionKey.interestOps() & ~interest);
    }

    public void registerChannelLater(final SelectableChannel selectableChannel, final int interest, final SelectorHandler selectorHandler, final ErrorHandler errorHandler){
        invokeLater(new Runnable(){
            public void run(){
                try{
                    registerChannelNow(selectableChannel, interest, selectorHandler);
                } catch(IOException x){
                    errorHandler.handleError(x);
                }
            }
        });
    }

    public void setChannelInterestLater(final SelectableChannel selectableChannel, final int interest, final boolean enable, final ErrorHandler errorHandler){
        invokeLater(new Runnable(){
            public void run(){
                try{
                    setChannelInterestNow(selectableChannel, interest, enable);
                } catch(IOException x){
                    errorHandler.handleError(x);
                }
            }
        });
    }

    public void run() {
        LOGGER.debug("dispatcher thread running");
        this.thread = Thread.currentThread();
        while(!kill){
            synchronized(laters){
                for (Runnable runnable: laters) runnable.run();
                laters.clear();
            }
            if (!kill){
                try{
                    if (selector.keys().size() > 16) LOGGER.debug("selector keys > 16: " + selector.keys().size());
                    // for (SelectionKey selectionKey: selector.keys()) if (selectionKey.isValid()) LOGGER.debug("selecting, interest ops: " + selectionKey.attachment() + ' ' + ((selectionKey.interestOps() & SelectionKey.OP_ACCEPT) != 0 ? 'A' : '-') + ((selectionKey.interestOps() & SelectionKey.OP_CONNECT) != 0 ? 'C' : '-') + ((selectionKey.interestOps() & SelectionKey.OP_READ) != 0 ? 'R' : '-') + ((selectionKey.interestOps() & SelectionKey.OP_WRITE) != 0 ? 'W' : '-'));
                    if (pulse > 0) selector.select(pulse); else selector.select();
                    for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext();){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        // LOGGER.debug("selection key: valid=" + selectionKey.isValid() + ", accept=" + selectionKey.isAcceptable() + ", connect=" + selectionKey.isConnectable() + ", read=" + selectionKey.isReadable() + ", write=" + selectionKey.isWritable());
                        selectionKey.interestOps(selectionKey.interestOps() & ~selectionKey.readyOps());
                        SelectorHandler selectorHandler = (SelectorHandler)selectionKey.attachment();
                        if (selectionKey.isAcceptable()){
                            selectorHandler.handleAccept();
                        } else if (selectionKey.isConnectable()){
                            selectorHandler.handleConnect();
                        } else if (selectionKey.isReadable()){
                            selectorHandler.handleRead();
                        } else if (selectionKey.isWritable()){
                            selectorHandler.handleWrite();
                        }
                    }
                    for (SelectionKey selectionKey:selector.keys()){
                        SelectorHandler selectorHandler = (SelectorHandler)selectionKey.attachment();
                        selectorHandler.pulse();
                    }
                } catch(IOException x){
                    LOGGER.error("dispatcher i/o error: ", x);
                } catch(Throwable t){
                    LOGGER.error("exiting selector loop: ", t);
                    kill = true;
                }
            }
        }
        for (SelectionKey selectionKey: selector.keys()){
            try{
                selectionKey.channel().close();
            } catch(IOException x2){
                LOGGER.error("cannot close channel", x2);
            }
        }
        try{
            selector.close();
        } catch(IOException x2){
            LOGGER.error("cannot close selector", x2);
        }
        LOGGER.debug("dispatcher thread stopped");
    }
}
