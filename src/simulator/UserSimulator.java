package simulator;

import model.*;

import java.rmi.server.UID;
import java.util.Arrays;
import java.util.List;

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

    public UserSimulator(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    @Override
    public void run() {
        while (running) {
            generateNewOrder();
            sendExistingOrder();
        }
    }

    private void sendExistingOrder() {
        //TODO get random REGISTERED order
        //TODO send it

    }

    private void generateNewOrder() {
        OrderImage newOrderImage = getNewOrder();
        orderModel.update(newOrderImage);
        try {
            Thread.sleep(2000);
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

    public void stop(){
        running = false;
    }
}
