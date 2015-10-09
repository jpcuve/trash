package api.transaction;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:31:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentEntity implements Entity {
    private Object key;
    private DecisionEntity decision;
    private String locale;
    private String location;

    public void setId(Object o) {
        this.key = o;
    }

    public DecisionEntity getDecision() {
        return decision;
    }

    public void setDecision(DecisionEntity decision) {
        this.decision = decision;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString() {
        return this.getClass().getName() + ":" + key;
    }
}
