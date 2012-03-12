package view;

import java.util.Date;
import model.OrderImage;
import model.OrderListener;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleViewer implements OrderListener{
    @Override
    public void newOrder(OrderImage orderImage) {
        System.out.println( new Date().toString() + " New order : \n" + orderImage);


    }

    @Override
    public void updateOrder(OrderImage orderImage) {
        System.out.println( new Date().toString() + " Updated order \n: " + orderImage);
    }
}
