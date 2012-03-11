package view;

import model.OrderField;
import model.OrderImage;
import model.OrderListener;

import java.util.Date;

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
        System.out.println( new Date().toString() + "New order : " + orderImage);
        for(OrderField field : orderImage.getFields()){
            System.out.print(field + " = " + orderImage.getValue(field) + "   ");
        }
        System.out.println();
    }

    @Override
    public void updateOrder(OrderImage orderImage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
