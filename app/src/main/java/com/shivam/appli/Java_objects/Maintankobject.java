package com.shivam.appli.Java_objects;

public class Maintankobject {

    String aadate;
    String abtime;
    String bparticular;
    String cpurchase;
    String dissue;
    String ebalance;
    String fCMS;
    String gdifference;

    public Maintankobject(){

    }

    public Maintankobject(String aadate,String abtime, String bparticular, String cpurchase, String dissue, String ebalance, String fCMS, String gdifference) {
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

    public String getCpurchase() {
        return cpurchase;
    }

    public void setCpurchase(String cpurchase) {
        this.cpurchase = cpurchase;
    }

    public String getDissue() {
        return dissue;
    }

    public void setDissue(String dissue) {
        this.dissue = dissue;
    }

    public String getEbalance() {
        return ebalance;
    }

    public void setEbalance(String ebalance) {
        this.ebalance = ebalance;
    }

    public String getfCMS() {
        return fCMS;
    }

    public void setfCMS(String fCMS) {
        this.fCMS = fCMS;
    }

    public String getGdifference() {
        return gdifference;
    }

    public void setGdifference(String gdifference) {
        this.gdifference = gdifference;
    }
}
