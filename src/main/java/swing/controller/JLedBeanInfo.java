package swing.controller;

import java.awt.*;
import java.beans.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 4, 2004
 * Time: 10:03:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class JLedBeanInfo implements BeanInfo {
    public int getDefaultEventIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getDefaultPropertyIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Image getIcon(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public BeanDescriptor getBeanDescriptor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        return new BeanInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return new EventSetDescriptor[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public MethodDescriptor[] getMethodDescriptors() {
        return new MethodDescriptor[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor background = new PropertyDescriptor("background", JLed.class);
            PropertyDescriptor foreground = new PropertyDescriptor("foreground", JLed.class);
            PropertyDescriptor font = new PropertyDescriptor("font", JLed.class);
            PropertyDescriptor label = new PropertyDescriptor("label", JLed.class);
            background.setBound(true);
            foreground.setBound(true);
            font.setBound(true);
            label.setBound(true);
            PropertyDescriptor rv[] = {background, foreground, font, label};
            return rv;
        } catch (IntrospectionException x) {
             throw new Error(x.toString());
        }
    }
}
