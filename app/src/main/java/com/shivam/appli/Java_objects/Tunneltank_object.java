package com.shivam.appli.Java_objects;

public class Tunneltank_object {
    String atime;
    String breading;
    String ctrolly;
    String ddiff;
    String eoutput1;
    String foutput2;
    String glastval;

    public Tunneltank_object(){

    }

    public Tunneltank_object(String atime, String breading, String ctrolly, String ddiff, String eoutput1, String foutput2,String glastval) {
        this.atime = atime;
        this.breading = breading;
        this.ctrolly = ctrolly;
        this.ddiff = ddiff;
        this.eoutput1 = eoutput1;
        this.foutput2 = foutput2;
        this.glastval=glastval;
    }

    public String getGlastval() {
        return glastval;
    }

    public void setGlastval(String glastval) {
        this.glastval = glastval;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getBreading() {
        return breading;
    }

    public void setBreading(String breading) {
        this.breading = breading;
    }

    public String getCtrolly() {
        return ctrolly;
    }

    public void setCtrolly(String ctrolly) {
        this.ctrolly = ctrolly;
    }

    public String getDdiff() {
        return ddiff;
    }

    public void setDdiff(String ddiff) {
        this.ddiff = ddiff;
    }

    public String getEoutput1() {
        return eoutput1;
    }

    public void setEoutput1(String eoutput1) {
        this.eoutput1 = eoutput1;
    }

    public String getFoutput2() {
        return foutput2;
    }

    public void setFoutput2(String foutput2) {
        this.foutput2 = foutput2;
    }
}
