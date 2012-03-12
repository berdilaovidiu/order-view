package simulator;

import model.OrderModel;
import view.ConsoleViewer;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimulatorApplication {
    public static void main(String[] args) {
        OrderModel orderModel = new OrderModel();
        orderModel.addListener(new ConsoleViewer());

        Runnable userSimulatorRunnable = new UserSimulator(orderModel);
        Thread userSimulatorThread = new Thread(userSimulatorRunnable);
        userSimulatorThread.start();

        Runnable brokerSimulatorRunnable = new BrokerSimulator(orderModel);
        Thread brokerSimulationThread = new Thread(brokerSimulatorRunnable);
        brokerSimulationThread.start();
    }
}
