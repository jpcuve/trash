package swing.mtswing;

import java.util.EventListener;

public interface MessageListener extends EventListener {
    void messageSent(MessageEvent event);
}
