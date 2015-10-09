package net.nio;

public interface SelectorHandler {
    void pulse();
    void handleAccept();
    void handleConnect();
    void handleRead();
    void handleWrite();
}
