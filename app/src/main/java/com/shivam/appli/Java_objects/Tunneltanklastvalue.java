package com.shivam.appli.Java_objects;

public class Tunneltanklastvalue {
    String date;
    double reading;

    public Tunneltanklastvalue(){

    }

    public Tunneltanklastvalue(String date, double reading) {
        this.date = date;
        this.reading = reading;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }
}
