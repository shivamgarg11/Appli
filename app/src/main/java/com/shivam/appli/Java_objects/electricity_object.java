package com.shivam.appli.Java_objects;

public class electricity_object {
    double akwh;
    double ckvah;
    double bdiffkwh;
    double ddiffkvah;
    double empf;
    double fppf;
    double gcal_pf;
    double hamount1;
    double iamount2;
    String aatime;

    public electricity_object(){

    }

    public electricity_object(double akwh, double ckvah, double bdiffkwh, double ddiffkvah, double empf, double fppf, double gcal_pf, double hamount1, double iamount2,String aatime) {
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
    }


    public double getAkwh() {
        return akwh;
    }

    public void setAkwh(double akwh) {
        this.akwh = akwh;
    }

    public double getCkvah() {
        return ckvah;
    }

    public void setCkvah(double ckvah) {
        this.ckvah = ckvah;
    }

    public double getBdiffkwh() {
        return bdiffkwh;
    }

    public void setBdiffkwh(double bdiffkwh) {
        this.bdiffkwh = bdiffkwh;
    }

    public double getDdiffkvah() {
        return ddiffkvah;
    }

    public void setDdiffkvah(double ddiffkvah) {
        this.ddiffkvah = ddiffkvah;
    }

    public double getEmpf() {
        return empf;
    }

    public void setEmpf(double empf) {
        this.empf = empf;
    }

    public double getFppf() {
        return fppf;
    }

    public void setFppf(double fppf) {
        this.fppf = fppf;
    }

    public double getGcal_pf() {
        return gcal_pf;
    }

    public void setGcal_pf(double gcal_pf) {
        this.gcal_pf = gcal_pf;
    }

    public double getHamount1() {
        return hamount1;
    }

    public void setHamount1(double hamount1) {
        this.hamount1 = hamount1;
    }

    public double getIamount2() {
        return iamount2;
    }

    public void setIamount2(double iamount2) {
        this.iamount2 = iamount2;
    }

    public String getTime() {
        return aatime;
    }

    public void setTime(String aatime) {
        this.aatime = aatime;
    }
}
