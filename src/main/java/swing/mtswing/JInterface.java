package swing.mtswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class JInterface extends JPanel {
    private JTextArea taOutput = new JTextArea();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ArrayList<FutureTask<String>> futureTasks = new ArrayList<FutureTask<String>>();

    private void addMessage(final String s){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                taOutput.setText(taOutput.getText() + String.format("%s%n", s));
            }
        });
    }

    public JInterface() {
        super(new BorderLayout());
        final JToolBar tbMain = new JToolBar();
        tbMain.add(new AbstractAction("start"){
            public void actionPerformed(ActionEvent e) {
                final LongTask longTask = new LongTask();
                longTask.addMessageListener(new MessageListener(){
                    public void messageSent(MessageEvent event) {
                        addMessage(event.getMessage());
                    }
                });
                final FutureTask<String> futureTask = new FutureTask<String>(longTask);
                futureTasks.add(futureTask);
                executorService.submit(futureTask);
            }
        });
        tbMain.add(new AbstractAction("stop"){
            public void actionPerformed(ActionEvent e) {
                executorService.shutdownNow();
            }
        });
        add(BorderLayout.PAGE_START, tbMain);
        add(BorderLayout.CENTER, new JScrollPane(taOutput));
    }

    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    public static void main(String[] args) {
        final JFrame fInterface = new JFrame();
        fInterface.add(new JInterface());
        fInterface.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fInterface.pack();
        fInterface.setVisible(true);
    }
}
