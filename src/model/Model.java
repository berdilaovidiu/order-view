package model;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class Model {
    private Map<UID, OrderImage> orderMap;
    private List<OrderListener> listeners;
    
    public Model() {
        orderMap = new HashMap<UID, OrderImage>();
        listeners = new ArrayList<OrderListener>();
    }
    
    public void addListener(OrderListener listener){
        listeners.add(listener);
    }
    
    public void removeListener (OrderListener listener){
        listeners.remove(listener);
    }


    public void addTransaction(OrderImage orderImage) {
        UID orderID = (UID) orderImage.getValue(OrderField.ORDER_ID);
        OrderImage existingImage = orderMap.get(orderID);
        if (existingImage == null) {
            orderMap.put(orderID, orderImage);
            //TODO notify listener
            for (OrderListener listener : listeners){
                listener.newOrder(orderImage);
            }
        }else {
            existingImage.mergeWith(orderImage);
            //TODO notify listener
            for (OrderListener listener : listeners){
                listener.orderUpdated(orderImage);
            }
        }
    }
}
