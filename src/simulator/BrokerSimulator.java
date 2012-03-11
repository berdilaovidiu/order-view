package simulator;

import model.OrderField;
import model.OrderImage;
import model.OrderModel;
import model.OrderState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/10/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrokerSimulator implements Runnable {
    public static final int RANDOM_BROKER_ACTION_RESPONSE_DELAY = 10000;
    private OrderModel orderModel;
    private Map<OrderState, OrderState> orderStateTransitionMap;

    public BrokerSimulator(OrderModel orderModel) {
        this.orderModel = orderModel;
        orderStateTransitionMap = new HashMap<OrderState, OrderState>();
        orderStateTransitionMap.put(OrderState.PENDING_SENT, OrderState.SENT);
        orderStateTransitionMap.put(OrderState.PENDING_AMEND, OrderState.SENT);
        orderStateTransitionMap.put(OrderState.SENT, OrderState.PARTIAL_FILLED);
        orderStateTransitionMap.put(OrderState.PARTIAL_FILLED, OrderState.FILLED);
    }

    @Override
    public void run() {
        while (true) {
            for (OrderImage order : orderModel.getOrders()) {
                OrderState state = (OrderState) order.getValue(OrderField.STATE);
                Double totalQuantity = (Double) order.getValue(OrderField.QUANTITY);
                try {
                    switch (state) {
                        case PENDING_SENT:
                            Thread.sleep((long) (Math.random() * RANDOM_BROKER_ACTION_RESPONSE_DELAY));
                            order.setField(OrderField.STATE, OrderState.SENT);
                            break;
                        case SENT:
                            Thread.sleep((long) (Math.random() * RANDOM_BROKER_ACTION_RESPONSE_DELAY));
                            order.setField(OrderField.STATE, OrderState.PARTIAL_FILLED);
                            order.setField(OrderField.FILLED_QUANTITY, getRandomFilledQuantity(totalQuantity));
                            break;
                        case PARTIAL_FILLED:
                            Thread.sleep((long) (Math.random() * RANDOM_BROKER_ACTION_RESPONSE_DELAY));
                            order.setField(OrderField.STATE, OrderState.FILLED);
                            order.setField(OrderField.FILLED_QUANTITY, totalQuantity);
                            break;
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                orderModel.update(order);
            }
        }
    }

    private double getRandomFilledQuantity(double quantity) {
        return Math.random() * quantity;
    }
}
