package swing.mtswing;

import java.util.EventObject;

public class MessageEvent extends EventObject {
    private String message;

    public MessageEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
