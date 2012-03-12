package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.OrderModel;
import simulator.BrokerSimulator;
import simulator.UserSimulator;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/4/12
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBirdsEyeView {
    public static void main(String args[]) {
        final BirdsEyeView view = new BirdsEyeView();

        OrderModel orderModel = new OrderModel();
        orderModel.addListener(view);

        Runnable orderGenerator = new UserSimulator(orderModel);
        Thread generatorThread = new Thread(orderGenerator);
        generatorThread.start();

        Runnable brokerSimulatorRunnable = new BrokerSimulator(orderModel);
        Thread brokerSimulationThread = new Thread(brokerSimulatorRunnable);
        brokerSimulationThread.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Birds Eye View");
                frame.setContentPane(view);

                frame.setSize(900, 900);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });


    }
}
