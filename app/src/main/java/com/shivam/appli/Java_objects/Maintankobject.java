package com.shivam.appli.Java_objects;

public class Maintankobject {

    String aadate;
    String abtime;
    String bparticular;
    double cpurchase;
    double dissue;
    double ebalance;
    double fCMS;
    double gdifference;

    public Maintankobject(){

    }

    public Maintankobject(String aadate,String abtime, String bparticular, double cpurchase, double dissue, double ebalance, double fCMS, double gdifference) {
        this.abtime=abtime;
        this.aadate = aadate;
        this.bparticular = bparticular;
        this.cpurchase = cpurchase;
        this.dissue = dissue;
        this.ebalance = ebalance;
        this.fCMS = fCMS;
        this.gdifference = gdifference;
    }


    public String getAbtime() {
        return abtime;
    }

    public void setAbtime(String abtime) {
        this.abtime = abtime;
    }

    public String getAadate() {
        return aadate;
    }

    public void setAadate(String aadate) {
        this.aadate = aadate;
    }

    public String getBparticular() {
        return bparticular;
    }

    public void setBparticular(String bparticular) {
        this.bparticular = bparticular;
    }

    public double getCpurchase() {
        return cpurchase;
    }

    public void setCpurchase(double cpurchase) {
        this.cpurchase = cpurchase;
    }

    public double getDissue() {
        return dissue;
    }

    public void setDissue(double dissue) {
        this.dissue = dissue;
    }

    public double getEbalance() {
        return ebalance;
    }

    public void setEbalance(double ebalance) {
        this.ebalance = ebalance;
    }

    public double getfCMS() {
        return fCMS;
    }

    public void setfCMS(double fCMS) {
        this.fCMS = fCMS;
    }

    public double getGdifference() {
        return gdifference;
    }

    public void setGdifference(double gdifference) {
        this.gdifference = gdifference;
    }
}
