package swing.autoscroll;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 4:22:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class HeaderModel {
    private List<Integer> extents = new ArrayList<Integer>();
    private int defaultExtent;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    public HeaderModel(int count, int extent) {
        this.defaultExtent = extent;
        for (int i = 0; i < count; i++) extents.add(extent);
    }

    public List<Integer> getExtents() {
        return extents;
    }

    public int getDefaultExtent() {
        return defaultExtent;
    }

    public void setExtent(int count, int extent){
        extents.set(count, extent);
        fireStateChanged();
    }

    public int getTotalExtent(int count){
        int tot = 0;
        for (int i = 0; i < count && i < extents.size(); i++) tot += extents.get(i);
        return tot;
    }

    public int getTotalExtent(){
        int tot = 0;
        for (int i: extents) tot += i;
        return tot;
    }

    public void addChangeListener(ChangeListener l){
        listeners.add(l);
    }

    public void removeChangeListener(ChangeListener l){
        listeners.remove(l);
    }

    protected void fireStateChanged(){
        for (ChangeListener listener: listeners) listener.stateChanged(new ChangeEvent(this));
    }
}
