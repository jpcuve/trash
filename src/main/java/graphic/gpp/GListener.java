package graphic.gpp;

import java.util.EventListener;

public interface GListener extends EventListener{ 

	public abstract void shapeMouseDown(GEvent e);
	public abstract void shapeMouseMove(GEvent e);
	public abstract void shapeMouseUp(GEvent e);
	public abstract void shapeClick(GEvent e);
	public abstract void shapeDoubleClick(GEvent e);

}

