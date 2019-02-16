package com.shivam.appli.Java_objects;

public class electricitylastvalue {
    String date;
    double kwh;
    double kvah;

    public electricitylastvalue(){

    }

    public electricitylastvalue(String date, double kwh, double kvah) {
        this.date = date;
        this.kwh = kwh;
        this.kvah = kvah;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getKwh() {
        return kwh;
    }

    public void setKwh(double kwh) {
        this.kwh = kwh;
    }

    public double getKvah() {
        return kvah;
    }

    public void setKvah(double kvah) {
        this.kvah = kvah;
    }
}
