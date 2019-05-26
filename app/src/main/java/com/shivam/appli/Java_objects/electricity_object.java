package com.shivam.appli.Java_objects;

public class electricity_object {
    String akwh;
    String ckvah;
    String bdiffkwh;
    String ddiffkvah;
    String empf;
    String fppf;
    String gcal_pf;
    String hamount1;
    String iamount2;
    String aatime;
    String glastval;



    public electricity_object(){

    }

    public electricity_object(String akwh, String ckvah, String bdiffkwh, String ddiffkvah, String empf, String fppf, String gcal_pf, String hamount1, String iamount2,String aatime,String glastval) {
        this.akwh = akwh;
        this.ckvah = ckvah;
        this.bdiffkwh = bdiffkwh;
        this.ddiffkvah = ddiffkvah;
        this.empf = empf;
        this.fppf = fppf;
        this.gcal_pf = gcal_pf;
        this.hamount1 = hamount1;
        this.iamount2 = iamount2;
        this.aatime=aatime;
        this.glastval=glastval;
    }


    public String getAkwh() {
        return akwh;
    }

    public void setAkwh(String akwh) {
        this.akwh = akwh;
    }

    public String getCkvah() {
        return ckvah;
    }

    public void setCkvah(String ckvah) {
        this.ckvah = ckvah;
    }

    public String getBdiffkwh() {
        return bdiffkwh;
    }

    public void setBdiffkwh(String bdiffkwh) {
        this.bdiffkwh = bdiffkwh;
    }

    public String getDdiffkvah() {
        return ddiffkvah;
    }

    public void setDdiffkvah(String ddiffkvah) {
        this.ddiffkvah = ddiffkvah;
    }

    public String getEmpf() {
        return empf;
    }

    public void setEmpf(String empf) {
        this.empf = empf;
    }

    public String getFppf() {
        return fppf;
    }

    public void setFppf(String fppf) {
        this.fppf = fppf;
    }

    public String getGcal_pf() {
        return gcal_pf;
    }

    public void setGcal_pf(String gcal_pf) {
        this.gcal_pf = gcal_pf;
    }

    public String getHamount1() {
        return hamount1;
    }

    public void setHamount1(String hamount1) {
        this.hamount1 = hamount1;
    }

    public String getIamount2() {
        return iamount2;
    }

    public void setIamount2(String iamount2) {
        this.iamount2 = iamount2;
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
