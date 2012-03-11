package view;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/10/12
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BirdsEyeViewLayout implements LayoutManager{
    @Override
    public void addLayoutComponent(String name, Component comp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void layoutContainer(Container parent) {
        for(Component component : parent.getComponents()){
            Point location = component.getLocation();
            Dimension size = component.getSize();
            component.setBounds(location.x,location.y,  size.width, size.height);
        }
    }
}
