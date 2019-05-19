package com.shivam.appli.Java_objects;

public class gaslastvalue {
    String date;
    String value;

    public gaslastvalue(){

    }

    public gaslastvalue(String date, String value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
