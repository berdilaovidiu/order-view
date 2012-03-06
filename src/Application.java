import model.Model;
import simulator.OrderGenerator;
import view.ConsoleViewer;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Application {
    public static void main(String[] args){
        Model model = new Model();
        model.addListener(new ConsoleViewer());

        Runnable orderGenerator = new OrderGenerator(model);
        Thread generatorThread = new Thread(orderGenerator);
        generatorThread.start();
    }
}
