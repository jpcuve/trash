package lang.dialect;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 26, 2004
 * Time: 5:25:54 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Construct extends Expression {
    protected Object tag = Construct.class.getName();

    protected Construct(Object tag) {
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isConstruct() {
        return true;
    }
}
