package com.whr.user.pojo;

/**
 * Created by lenovo on 5/15/2017.
 */


public class Clinicname {
    private String hospname;
    private int hid;

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    @Override
    public String toString() {
        return "Clinicname{" +
                "hospname='" + hospname + '\'' +
                ", hid=" + hid +
                '}';
    }
}
