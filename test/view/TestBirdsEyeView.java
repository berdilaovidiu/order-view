package view;

import model.Model;
import simulator.OrderGenerator;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/4/12
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBirdsEyeView {
    public static void main(String args[]){
        final BirdsEyeView view = new BirdsEyeView();

        Model model = new Model();
        model.addListener(view);

        Runnable orderGenerator = new OrderGenerator(model);
        Thread generatorThread = new Thread(orderGenerator);
        generatorThread.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Birds Eye View");

                frame.setContentPane(view);


                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });


    }
}
