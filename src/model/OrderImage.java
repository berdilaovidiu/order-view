package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ovidiu
 * Date: 3/3/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderImage {
    private Map<OrderField, Object> fieldMap;

    public OrderImage() {
        fieldMap = new HashMap<OrderField, Object>();
    }

    public void setField(OrderField field, Object value) {
        fieldMap.put(field, field.getKlass().cast(value));
    }

    public Object getValue(OrderField field) {
        return fieldMap.get(field);
    }
    
    public Set<OrderField> getFields(){
        return new HashSet<OrderField>(fieldMap.keySet());
    }
    
    public void mergeWith(OrderImage orderImage){
        for (OrderField field : orderImage.getFields()){
            Object value = orderImage.getValue(field);
            fieldMap.put(field, value);
        }
    }
}
