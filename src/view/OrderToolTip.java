package view;

import java.awt.*;
import javax.swing.JComponent;
import model.OrderImage;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/4/12
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderToolTip extends JComponent {
    private OrderImage orderImage;

    public OrderToolTip(OrderImage orderImage) {
        this.orderImage = orderImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Color.lightGray);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2d.setColor(Color.BLACK);
        String orderString = getOrderString();
        String[] tokens = orderString.split("\\n");
        int hpos = 20;
        for (String token : tokens) {
            g2d.drawString(token, 10, hpos);
            hpos +=20;
        }

        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
    }

    public String getOrderString() {
        return orderImage.toString();
    }
}
