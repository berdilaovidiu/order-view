package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import model.OrderField;
import model.OrderImage;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/4/12
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderView extends JComponent {
    private static Color COLOR_PENDING_QUANTITY = Color.BLUE;
    private static Color COLOR_FILLED_QUANTITY = Color.RED;
    public static final int STROKE_SIZE = 1;

    private OrderImage orderImage;

    public OrderView(final OrderImage orderImage) {
        this.orderImage = orderImage;
        this.addMouseListener(new MouseListenerImpl());
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(STROKE_SIZE));

        g2d.setPaint(new RadialGradientPaint(getWidth() / 2, getHeight() / 2, getWidth() / 2, new float[]{0.0f, 1.0f}, new Color[]{Color.cyan, COLOR_PENDING_QUANTITY}));
        g2d.fillOval(STROKE_SIZE / 2, STROKE_SIZE / 2, getWidth() - STROKE_SIZE - 1, getHeight() - STROKE_SIZE - 1);

        double percentage = getPercentageFilled();
        int percent = (int) (360 * percentage);
        g2d.setPaint(new RadialGradientPaint(getWidth() / 2, getHeight() / 2, getWidth() / 2, new float[]{0.0f, 1.0f}, new Color[]{Color.white, COLOR_FILLED_QUANTITY}));
        g2d.fillArc(STROKE_SIZE / 2, STROKE_SIZE / 2, getWidth() - STROKE_SIZE - 1, getHeight() - STROKE_SIZE - 1, 90, -percent);

        g2d.setColor(Color.BLACK);
        g2d.drawOval(STROKE_SIZE / 2, STROKE_SIZE / 2, getWidth() - STROKE_SIZE - 1, getHeight() - STROKE_SIZE - 1);
    }

    private double getPercentageFilled() {
        Double totalQuantity = (Double) orderImage.getValue(OrderField.QUANTITY);
        Double filledQuantity = (Double) orderImage.getValue(OrderField.FILLED_QUANTITY);
        return filledQuantity / totalQuantity;
    }

    public OrderImage getOrderImage() {
        return orderImage;
    }

    private class MouseListenerImpl implements MouseListener {
        OrderToolTip toolTip = null;

        @Override
        public void mouseClicked(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (toolTip == null) {
                toolTip = new OrderToolTip(orderImage);
                OrderView.this.getParent().add(toolTip);
                Point location = OrderView.this.getLocation();
                Point toolTipLocation = new Point();
                toolTipLocation.setLocation(location.x, location.y - 80);
                toolTip.setLocation(toolTipLocation);
                toolTip.setSize(300, 200);
            } else {
                toolTip.setVisible(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            toolTip.setVisible(false);
            OrderView.this.repaint();
        }
    }

}
