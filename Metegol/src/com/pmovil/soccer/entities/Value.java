package com.pmovil.soccer.entities;

public class Value extends Data {

    private String value = new String();

    public Value() {
    }

    public Value(Value value) {
        this.id = value.id;
        this.value = value.value;
    }

    public Value copy() {
        return new Value(this);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
