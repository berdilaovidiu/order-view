package view;

import model.OrderField;
import model.OrderImage;
import model.OrderListener;
import model.OrderState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
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
    private Set<Shape> sectors = new HashSet<Shape>();

    static {
        stateColorMap.put(OrderState.REGISTERED, Color.BLUE.brighter().brighter());
        stateColorMap.put(OrderState.SENT, Color.PINK);
        stateColorMap.put(OrderState.FILLED, Color.GREEN);
    }

    private List<OrderView> orderList = new ArrayList<OrderView>();
    private OrderField groupingCriteria = OrderField.COUNTRY;
    private Map<OrderState, Shape> stateShapeMap = new HashMap<OrderState, Shape>();
    private Map<Object, Shape> groupingSliceShapeMap = new TreeMap<Object, Shape>();
    private static final int MAXIMUM_ORDER_SIZE = 20;
    private static final int MINIMUM_ORDER_SIZE = 4;
    private static final int MAXIMUM_ORDER_QUANTITY = 5000;

    public BirdsEyeView() {
        setLayout(new BirdsEyeViewLayout());
        MouseAdapterImpl mouseListener = new MouseAdapterImpl();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public void addOrderView(OrderView orderView) {
        orderList.add(orderView);
//        orderView.setSize();
//        orderView.setLocation();
        add(orderView);
        revalidate();
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
        ((Graphics2D) g).setBackground(Color.white);
        g.clearRect(0, 0, getWidth(), getHeight());
        drawStates(g);
        drawSlices(g);
//        for(Shape sector: sectors){
//            ((Graphics2D) g).setStroke(new BasicStroke(4));
//            g.setColor(Color.GREEN);
//            ((Graphics2D) g).draw(sector);
//        }
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
        int size = (int) (quantity * MAXIMUM_ORDER_SIZE / MAXIMUM_ORDER_QUANTITY);
        int finalSize = Math.max(MINIMUM_ORDER_SIZE, size);
        orderView.setSize(finalSize, finalSize);
        //todo compute location
        try {
            orderView.setLocation(getRandomLocation(orderImage, finalSize));
        } catch (Exception ex) {
            orderView.setLocation((int) (Math.random() * getWidth()), (int) (Math.random() * getHeight()));
        }
        addOrderView(orderView);
    }

    private Point getRandomLocation(OrderImage orderImage, int size) {
        try {
            Object valueOfGroupingCriteria = orderImage.getValue(groupingCriteria);
            Object orderState = orderImage.getValue(OrderField.STATE);
            Shape ellipse;
            switch ((OrderState) orderState) {
                case REGISTERED:
                case PENDING_SENT:
                    ellipse = stateShapeMap.get(OrderState.REGISTERED);
                    break;
                case SENT:
                case PARTIAL_FILLED:
                    ellipse = stateShapeMap.get(OrderState.SENT);
                    break;
                case FILLED:
                case OVER_FILLED:
                    ellipse = stateShapeMap.get(OrderState.FILLED);
                    break;
                default:
                    ellipse = stateShapeMap.get(OrderState.REGISTERED);
            }

            Shape slice = groupingSliceShapeMap.get(valueOfGroupingCriteria);
            Area sliceArea = new Area(slice);
            Area ellipseArea = new Area(ellipse);
            sliceArea.intersect(ellipseArea);
            sectors.add(sliceArea);
            Rectangle bounds = sliceArea.getBounds();
            while (true) {
                double x = bounds.getX() + Math.random() * bounds.getWidth();
                double y = bounds.getY() + Math.random() * bounds.getHeight();
                Point point = new Point((int) x, (int) y);
                Rectangle targetedOrderArea = new Rectangle(point, new Dimension(size, size));
                boolean containsOrderArea = sliceArea.contains(targetedOrderArea);
                boolean instanceOfOrderView = getComponentAt(point) instanceof OrderView;
                if (containsOrderArea && !instanceOfOrderView) {
                    return point;
                }
//            System.out.println("contains order area = " + containsOrderArea);
//            System.out.println("instanceOfOrderView = " + instanceOfOrderView);
//            System.out.println("searching for uncovered area ... " + targetedOrderArea.getBounds() + " in " + sliceArea.getBounds());
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return new Point();
        }

    }

    @Override
    public void updateOrder(OrderImage orderImage) {
        //TODO get order view
        for (Component component : this.getComponents()) {
            if (component instanceof OrderView) {
                OrderView orderView = (OrderView) component;
                OrderState state = (OrderState) orderImage.getValue(OrderField.STATE);
                if (state.equals(OrderState.SENT) || state.equals(OrderState.FILLED)) {
                    if (orderView.getOrderImage().getValue(OrderField.ORDER_ID).equals(orderImage.getValue(OrderField.ORDER_ID))) {
                        orderView.setLocation(getRandomLocation(orderImage, orderView.getSize().width));
                        repaint();
                    }
                }
                orderView.repaint();
            }
        }
//        System.out.println("orderImage = " + orderImage);
    }

    private class MouseAdapterImpl extends MouseAdapter {
        OrderView orderUnderMouse = null;
        OrderToolTip toolTip = null;

        @Override
        public void mouseMoved(MouseEvent e) {
            Component componentUnderMouse = BirdsEyeView.this.getComponentAt(e.getPoint());
            if (componentUnderMouse instanceof OrderView) {
                OrderView currentOrderUnderMouse = (OrderView) componentUnderMouse;
                if (currentOrderUnderMouse != orderUnderMouse) {
                    hideToolTip();

                    if (orderUnderMouse != null) {
                        orderUnderMouse.setMouseOver(false);
                        orderUnderMouse.repaint();
                    }

                    orderUnderMouse = currentOrderUnderMouse;
                    orderUnderMouse.setMouseOver(true);
                    orderUnderMouse.repaint();

                    toolTip = new OrderToolTip(orderUnderMouse.getOrderImage());
                    BirdsEyeView.this.add(toolTip, -1);
                    Point location = orderUnderMouse.getLocation();
                    Point toolTipLocation = new Point();
                    toolTip.setSize(300, 200);
                    int prefLocation_x = location.x;
                    int prefLocation_y = location.y - toolTip.getHeight() - 10;
                    toolTipLocation.setLocation(Math.min(prefLocation_x, getWidth() - toolTip.getWidth()),
                            Math.max(prefLocation_y, 0));
                    toolTip.setLocation(toolTipLocation);
                    BirdsEyeView.this.validate();
                    BirdsEyeView.this.repaint();
                }
            } else {
                hideToolTip();

                if (orderUnderMouse != null) {
                    orderUnderMouse.setMouseOver(false);
                    orderUnderMouse.repaint();
                    orderUnderMouse = null;
                }
            }

        }

        private void hideToolTip() {
            if (toolTip != null) {
                BirdsEyeView.this.remove(toolTip);
                toolTip = null;
                BirdsEyeView.this.validate();
                BirdsEyeView.this.repaint();
            }
        }
    }
}
