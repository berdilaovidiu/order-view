package simulator;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Country;
import model.OrderField;
import model.OrderImage;
import model.OrderModel;
import model.OrderState;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserSimulator implements Runnable {
    private OrderModel orderModel;

    private List<String> instruments = Arrays.asList("6758", "7501", "8442", "4567", "JNIH2");
    private boolean running = true;

//    private List<Runnable> executingTasks = new ArrayList<Runnable>();

    public UserSimulator(OrderModel orderModel) {
        this.orderModel = orderModel;
//        executingTasks.add(getOrderGeneratorRunnable());
//        executingTasks.add(getOrderSenderRunnable());
    }

    @Override
    public void run() {
        Thread orderGeneratorThread = new Thread(getOrderGeneratorRunnable());
        Thread orderSenderThread = new Thread(getOrderSenderRunnable());
        orderGeneratorThread.start();
        orderSenderThread.start();

    }

    public Runnable getOrderGeneratorRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                while (running) {

                    generateNewOrder();
                }
            }
        };

    }

    public Runnable getOrderSenderRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                while (running) {

                    sendExistingOrder();
                }
            }
        };

    }

    private void sendExistingOrder() {
        //TODO get random REGISTERED order
        try {
            Thread.sleep(3000);
            List<OrderImage> registeredOrders = new ArrayList<OrderImage>();
            for (OrderImage order : orderModel.getOrders()) {
                OrderState state = (OrderState) order.getValue(OrderField.STATE);
                if (state.equals(OrderState.REGISTERED)) {
                    registeredOrders.add(order);
                }
            }
            OrderImage randomRegisteredOrder = registeredOrders.get((int) (Math.random() * registeredOrders.size()));
            //TODO send it
            randomRegisteredOrder.setField(OrderField.STATE, OrderState.PENDING_SENT);

            orderModel.update(randomRegisteredOrder);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void generateNewOrder() {
        try {
            Thread.sleep(2000);
            OrderImage newOrderImage = getNewOrder();
            orderModel.update(newOrderImage);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private OrderImage getNewOrder() {
        OrderImage newOrderImage = new OrderImage();
        newOrderImage.setField(OrderField.ORDER_ID, new UID());
        newOrderImage.setField(OrderField.INSTRUMENT, getRandomInstrument());
        Country country = getRandomCountry();
        String exchange = country.getRandomExchange();
        newOrderImage.setField(OrderField.COUNTRY, country);
        newOrderImage.setField(OrderField.EXCHANGE, exchange);
        newOrderImage.setField(OrderField.PRICE, getRandomPrice());
        newOrderImage.setField(OrderField.QUANTITY, getRandomQuantity());
        newOrderImage.setField(OrderField.FILLED_QUANTITY, new Double(0));
        newOrderImage.setField(OrderField.STATE, OrderState.REGISTERED);
        return newOrderImage;
    }

    private String getRandomInstrument() {
        int nrOfInstruments = instruments.size();
        int index = (int) (Math.random() * nrOfInstruments);
        return instruments.get(index);
    }

    private Country getRandomCountry() {
        int nrOfCountries = Country.values().length;
        int index = (int) (Math.random() * nrOfCountries);
        return Country.values()[index];
    }

    private Double getRandomPrice() {
        return Math.random() * 5000;
    }

    private Double getRandomQuantity() {
        return Math.random() * 10000;
    }

    public void stop() {
        running = false;
    }
}
