package model;

import java.rmi.server.UID;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderModel {
    private Map<UID, OrderImage> orderMap;
    private List<OrderListener> listeners;
    
    public OrderModel() {
        orderMap = new HashMap<UID, OrderImage>();
        listeners = new ArrayList<OrderListener>();
    }
    
    public void addListener(OrderListener listener){
        listeners.add(listener);
    }
    
    public void removeListener (OrderListener listener){
        listeners.remove(listener);
    }


    public synchronized void update(OrderImage orderImage) {
        UID orderID = (UID) orderImage.getValue(OrderField.ORDER_ID);
        OrderImage existingImage = orderMap.get(orderID);
        if (existingImage == null) {
            orderMap.put(orderID, orderImage);
            notifyNewOrder(orderImage);

        }else {
            existingImage.mergeWith(orderImage);
            notifyUpdateOrder(orderImage);

        }
    }

    private void notifyUpdateOrder(OrderImage orderImage) {
        for (OrderListener listener : listeners){
            listener.updateOrder(orderImage);
        }
    }

    private void notifyNewOrder(OrderImage orderImage) {
        for (OrderListener listener : listeners){
            listener.newOrder(orderImage);
        }
    }

    public synchronized Set<OrderImage> getOrders(){
        return new HashSet<OrderImage>(orderMap.values());
    }
}
