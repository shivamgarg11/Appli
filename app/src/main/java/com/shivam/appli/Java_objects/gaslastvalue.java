package com.shivam.appli.Java_objects;

public class gaslastvalue {
    String date;
    double value;

    public gaslastvalue(){

    }

    public gaslastvalue(String date, double value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
