package model;

import java.rmi.server.UID;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public enum OrderField {
    ORDER_ID(UID.class),
    INSTRUMENT(String.class),
    BROKER(String.class),
    EXCHANGE(String.class),
    ACCOUNT(String.class),
    TAG(String.class),
    PRICE(Double.class),
    QUANTITY(Double.class),
    FILLED_QUANTITY(Double.class),
    STATE(OrderState.class),

    COUNTRY(Country.class);
    Class klass;

    private OrderField(Class klass) {
        this.klass = klass;
    }

    public Class getKlass() {
        return klass;
    }
}
