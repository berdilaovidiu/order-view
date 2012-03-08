package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.OrderField;
import model.OrderImage;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/4/12
 * Time: 9:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestOrderView {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("Order View");
                JPanel panel = new JPanel();
                panel.setLayout(null);
                frame.setContentPane(panel);

                final OrderImage orderImage = new OrderImage();
                orderImage.setField(OrderField.QUANTITY, new Double(500));
                orderImage.setField(OrderField.FILLED_QUANTITY, new Double(100));
                final OrderView comp1 = new OrderView(orderImage);

                panel.add(comp1);
                comp1.setLocation(100, 100);
                comp1.setSize(100, 100);


                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        orderImage.setField(OrderField.FILLED_QUANTITY, Math.random() * 500);
                        comp1.repaint();
                    }
                });
                timer.start();

                frame.setSize(400, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });

    }
}
