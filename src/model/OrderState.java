package model;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
public enum OrderState {
    REGISTERED,
    PENDING_SENT,
    SENT,
    PARTIAL_FILLED,
    FILLED,
    OVER_FILLED,
    PENDING_AMEND,
    CANCELED,
    PENDING_CANCEL;

    public static OrderState getNextStateOf(OrderState state) {
        switch (state) {
            case REGISTERED:
                return PENDING_SENT;
            case PENDING_SENT:
                return SENT;
            case SENT:
                return PARTIAL_FILLED;
            case PARTIAL_FILLED:
                return FILLED;
            case PENDING_CANCEL:
                return CANCELED;
            default:
                return state;
        }
    }
}
