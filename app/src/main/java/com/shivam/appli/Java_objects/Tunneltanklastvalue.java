package com.shivam.appli.Java_objects;

public class Tunneltanklastvalue {
    String date;
    String reading;

    public Tunneltanklastvalue(){

    }

    public Tunneltanklastvalue(String date, String reading) {
        this.date = date;
        this.reading = reading;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }
}
