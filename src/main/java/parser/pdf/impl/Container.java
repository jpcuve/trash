package parser.pdf.impl;

import java.util.Collection;

public interface Container {
    Context getContext();
    void setContext(Context context);
    long getPosition();
    void setPosition(long position);
    Collection<Object> getChildren();
}
