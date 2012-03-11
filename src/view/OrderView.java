package view;

import model.OrderField;
import model.OrderImage;

import javax.swing.*;
import java.awt.*;

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
    private boolean mouseOver = false;

    public OrderView(final OrderImage orderImage) {
        this.orderImage = orderImage;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
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

        if(isMouseOver()){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.setPaint(new RadialGradientPaint(getWidth() / 2, getHeight() / 2, getWidth() / 2, new float[]{0.0f, 1.0f}, new Color[]{Color.white, Color.black}));
            g2d.fillOval(STROKE_SIZE / 2, STROKE_SIZE / 2, getWidth() - STROKE_SIZE - 1, getHeight() - STROKE_SIZE - 1);
        }
    }

    private double getPercentageFilled() {
        Double totalQuantity = (Double) orderImage.getValue(OrderField.QUANTITY);
        Double filledQuantity = (Double) orderImage.getValue(OrderField.FILLED_QUANTITY);
        return filledQuantity / totalQuantity;
    }

    public OrderImage getOrderImage() {
        return orderImage;
    }
}
