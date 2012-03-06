package view;

import model.OrderField;
import model.OrderImage;
import model.OrderListener;
import model.OrderState;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/4/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class BirdsEyeView extends JPanel implements OrderListener {
    private static List<Color> colorList = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.CYAN);
    private static Map<OrderState, Color> stateColorMap = new TreeMap<OrderState, Color>();

    static {
        stateColorMap.put(OrderState.REGISTERED, Color.BLUE.brighter().brighter());
        stateColorMap.put(OrderState.SENT, Color.PINK);
        stateColorMap.put(OrderState.FILLED, Color.GREEN);
    }

    private List<OrderView> orderList = new ArrayList<OrderView>();
    private OrderField groupingCriteria = OrderField.COUNTRY;
    private Map<OrderState, Shape> stateShapeMap = new HashMap<OrderState, Shape>();
    private Map<Object, Shape> groupingSliceShapeMap = new TreeMap<Object, Shape>();

    public BirdsEyeView() {
        setLayout(null);
    }

    public void addOrderView(OrderView orderView) {
        orderList.add(orderView);
//        orderView.setSize();
//        orderView.setLocation();
        add(orderView);
        repaint();
    }

    private Set<Object> getSlices() {
        Set<Object> result = new HashSet<Object>();
        for (OrderView orderView : orderList) {
            Object value = orderView.getOrderImage().getValue(groupingCriteria);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawStates(g);
        drawSlices(g);
    }

    private void drawStates(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int nrOfStates = stateColorMap.size();
        Ellipse2D prevEllipse = null;
        OrderState[] states = stateColorMap.keySet().toArray(new OrderState[stateColorMap.size()]);
        for (int index = 0; index < states.length; index++) {
            OrderState state = states[index];
            g.setColor(stateColorMap.get(state));
            Ellipse2D.Double ellipse = new Ellipse2D.Double(getWidth() * index / (2 * nrOfStates), getHeight() * index / (2 * nrOfStates), getWidth() * (nrOfStates - index) / nrOfStates - 1, getHeight() * (nrOfStates - index) / nrOfStates - 1);
            ((Graphics2D) g).fill(ellipse);
            if (prevEllipse != null) {
                Area prevArea = new Area(prevEllipse);
                Area area = new Area(ellipse);
                prevArea.subtract(area);
                stateShapeMap.put(states[index - 1], prevArea);
            }
            if (index == states.length - 1) {
                stateShapeMap.put(state, new Area(ellipse));
            }
            prevEllipse = ellipse;
        }
    }

    private void drawSlices(Graphics g) {
        Set<Object> slices = getSlices();
        Object[] sliceArray = slices.toArray(new Object[slices.size()]);
        int numberOfSlices = slices.size();
        int angle = 360 / numberOfSlices;
        int startAngle = -90;
        Composite prevComposite = ((Graphics2D) g).getComposite();
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        for (int index = 0; index < numberOfSlices; index++) {
            g.setColor(colorList.get(index));
            Arc2D.Double arc = new Arc2D.Double(0, 0, getWidth() - 1, getHeight() - 1, startAngle, -angle, Arc2D.PIE);
            ((Graphics2D) g).fill(arc);
            groupingSliceShapeMap.put(sliceArray[index], arc);
            startAngle -= angle;
        }
        ((Graphics2D) g).setComposite(prevComposite);
    }


    @Override
    public void newOrder(OrderImage orderImage) {
        OrderView orderView = new OrderView(orderImage);
        orderList.add(orderView);
        repaint();
        //todo compute size
        Double quantity = (Double) orderImage.getValue(OrderField.QUANTITY);
        int maximumSize = 30;
        int maximumQuantity = 5000;
        int size = (int) (quantity * maximumSize / maximumQuantity);
        orderView.setSize(size, size);
        //todo compute location
        try {
            orderView.setLocation(getRandomLocation(orderImage));
        } catch (Exception ex) {
            orderView.setLocation((int) (Math.random() * getWidth()), (int) (Math.random() * getHeight()));
        }
        add(orderView);
        repaint();
    }

    private Point getRandomLocation(OrderImage orderImage) {
        Object valueOfGroupingCriteria = orderImage.getValue(groupingCriteria);
        Object orderState = orderImage.getValue(OrderField.STATE);
        Shape slice = groupingSliceShapeMap.get(valueOfGroupingCriteria);
        Shape ellipse = stateShapeMap.get(orderState);
        Area sliceArea = new Area(slice);
        Area ellipseArea = new Area(ellipse);
        sliceArea.intersect(ellipseArea);

        Rectangle bounds = sliceArea.getBounds();
        while (true) {
            double x = bounds.getX() + Math.random() * bounds.getWidth();
            double y = bounds.getY() + Math.random() * bounds.getHeight();
            Point point = new Point((int) x, (int) y);
            if (sliceArea.contains(point)) {
                return point;
            }
        }

    }

    @Override
    public void orderUpdated(OrderImage orderImage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
