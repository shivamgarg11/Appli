package com.shivam.appli.Java_objects;

public class gas_object {
    double ainput;
    double bdifference;
    double cscm;
    double dmmbto;
    double eride;
    double fbill;

    public gas_object(){

    }

    public gas_object(double ainput, double bdifference, double cscm, double dmmbto, double eride, double fbill) {
        this.ainput = ainput;
        this.bdifference = bdifference;
        this.cscm = cscm;
        this.dmmbto = dmmbto;
        this.eride = eride;
        this.fbill = fbill;
    }


    public double getAinput() {
        return ainput;
    }

    public void setAinput(double ainput) {
        this.ainput = ainput;
    }

    public double getBdifference() {
        return bdifference;
    }

    public void setBdifference(double bdifference) {
        this.bdifference = bdifference;
    }

    public double getCscm() {
        return cscm;
    }

    public void setCscm(double cscm) {
        this.cscm = cscm;
    }

    public double getDmmbto() {
        return dmmbto;
    }

    public void setDmmbto(double dmmbto) {
        this.dmmbto = dmmbto;
    }

    public double getEride() {
        return eride;
    }

    public void setEride(double eride) {
        this.eride = eride;
    }

    public double getFbill() {
        return fbill;
    }

    public void setFbill(double fbill) {
        this.fbill = fbill;
    }
}
