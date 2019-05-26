package com.shivam.appli.Java_objects;

public class gas_object {
    String ainput;
    String bdifference;
    String cscm;
    String dmmbto;
    String eride;
    String fbill;
    String aatime;
    String glastval;



    public gas_object(){

    }

    public gas_object(String ainput, String bdifference, String cscm, String dmmbto, String eride, String fbill,String aatime,String glastval) {
        this.ainput = ainput;
        this.bdifference = bdifference;
        this.cscm = cscm;
        this.dmmbto = dmmbto;
        this.eride = eride;
        this.fbill = fbill;
        this.aatime=aatime;
        this.glastval=glastval;
    }


    public String getAinput() {
        return ainput;
    }

    public void setAinput(String ainput) {
        this.ainput = ainput;
    }

    public String getBdifference() {
        return bdifference;
    }

    public void setBdifference(String bdifference) {
        this.bdifference = bdifference;
    }

    public String getCscm() {
        return cscm;
    }

    public void setCscm(String cscm) {
        this.cscm = cscm;
    }

    public String getDmmbto() {
        return dmmbto;
    }

    public void setDmmbto(String dmmbto) {
        this.dmmbto = dmmbto;
    }

    public String getEride() {
        return eride;
    }

    public void setEride(String eride) {
        this.eride = eride;
    }

    public String getFbill() {
        return fbill;
    }

    public void setFbill(String fbill) {
        this.fbill = fbill;
    }

    public String getTime() {
        return aatime;
    }

    public void setTime(String aatime) {
        this.aatime = aatime;
    }

    public String getGlastval() {
        return glastval;
    }

    public void setGlastval(String glastval) {
        this.glastval = glastval;
    }
}
