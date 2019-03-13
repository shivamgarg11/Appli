package com.shivam.appli.Java_objects;

public class Tunneltank_object {
    String atime;
    double breading;
    double ctrolly;
    double ddiff;
    double eoutput1;
    double foutput2;

    public Tunneltank_object(){

    }

    public Tunneltank_object(String atime, double breading, double ctrolly, double ddiff, double eoutput1, double foutput2) {
        this.atime = atime;
        this.breading = breading;
        this.ctrolly = ctrolly;
        this.ddiff = ddiff;
        this.eoutput1 = eoutput1;
        this.foutput2 = foutput2;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public double getBreading() {
        return breading;
    }

    public void setBreading(double breading) {
        this.breading = breading;
    }

    public double getCtrolly() {
        return ctrolly;
    }

    public void setCtrolly(double ctrolly) {
        this.ctrolly = ctrolly;
    }

    public double getDdiff() {
        return ddiff;
    }

    public void setDdiff(double ddiff) {
        this.ddiff = ddiff;
    }

    public double getEoutput1() {
        return eoutput1;
    }

    public void setEoutput1(double eoutput1) {
        this.eoutput1 = eoutput1;
    }

    public double getFoutput2() {
        return foutput2;
    }

    public void setFoutput2(double foutput2) {
        this.foutput2 = foutput2;
    }
}
