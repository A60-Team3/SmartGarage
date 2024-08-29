package org.example.smartgarage.utils.filtering;

public class SearchCriteria {
    private String field;
    private TimeOperator timeOperator;


    private Object value;
    private Object value2;

    public SearchCriteria() {
    }

    public SearchCriteria(String field, TimeOperator timeOperator, Object value, Object value2) {
        this.field = field;
        this.timeOperator = timeOperator;
        this.value = value;
        this.value2 = value2;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    public TimeOperator getTimeOperator() {
        return timeOperator;
    }

    public void setTimeOperator(TimeOperator timeOperator) {
        this.timeOperator = timeOperator;
    }

}
