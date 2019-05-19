package com.shivam.appli.Java_objects;

public class electricitylastvalue {
    String date;
    String kwh;
    String kvah;

    public electricitylastvalue(){

    }

    public electricitylastvalue(String date, String kwh, String kvah) {
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

    public String getKwh() {
        return kwh;
    }

    public void setKwh(String kwh) {
        this.kwh = kwh;
    }

    public String getKvah() {
        return kvah;
    }

    public void setKvah(String kvah) {
        this.kvah = kvah;
    }
}
