package model;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OrderListener {
    
    public void newOrder(OrderImage orderImage);
    
    public void updateOrder(OrderImage orderImage);
}
