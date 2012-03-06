package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Country {
    JAPAN(Arrays.asList("T", "OS", "JNI", "NXT")),
    GERMANY(Arrays.asList("BER", "HBG")),
    FRANCE(Arrays.asList("PA", "LI")),
    ROMANIA(Arrays.asList("BUC", "SB")),
    ;

    private List<String> exchanges;

    private Country(List<String> exchanges) {
        this.exchanges = exchanges;
    }

    public String getRandomExchange() {
        int nrOfExchanges = exchanges.size();
        int index = (int) (Math.random() * nrOfExchanges);
        return exchanges.get(index);
    }
}
