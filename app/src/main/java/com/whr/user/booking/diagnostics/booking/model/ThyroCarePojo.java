package com.whr.user.booking.diagnostics.booking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ThyroCarePojo implements Parcelable {
    private String hc;
    private String b2c;
    private String name;
    private String pay_amt;
    private String code;
    private String testName = "";
    private String type = "";
    private String fasting = "";
    private boolean isCheck = false;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    ArrayList<Child> child;

    @Override
    public String toString() {
        return "{" +
                "hc='" + hc + '\'' +
                ", b2c='" + b2c + '\'' +
                ", name='" + name + '\'' +
                ", pay_amt='" + pay_amt + '\'' +
                ", code='" + code + '\'' +
                ", testName='" + testName + '\'' +
                ", type='" + type + '\'' +
                ", fasting='" + fasting + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }

    public ArrayList<Child> getChild() {
        return child;
    }

    public void setChild(ArrayList<Child> child) {
        this.child = child;
    }

    public String getHc() {
        return hc;
    }

    public void setHc(String hc) {
        this.hc = hc;
    }

    public String getB2c() {
        return b2c;
    }

    public void setB2c(String b2c) {
        this.b2c = b2c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPay_amt() {
        return pay_amt;
    }

    public void setPay_amt(String pay_amt) {
        this.pay_amt = pay_amt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFasting() {
        return fasting;
    }

    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }


    protected ThyroCarePojo(Parcel in) {
        hc = in.readString();
        b2c = in.readString();
        name = in.readString();
        pay_amt = in.readString();
        code = in.readString();
        testName = in.readString();
        type = in.readString();
        fasting = in.readString();
        isCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hc);
        dest.writeString(b2c);
        dest.writeString(name);
        dest.writeString(pay_amt);
        dest.writeString(code);
        dest.writeString(testName);
        dest.writeString(type);
        dest.writeString(fasting);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThyroCarePojo> CREATOR = new Creator<ThyroCarePojo>() {
        @Override
        public ThyroCarePojo createFromParcel(Parcel in) {
            return new ThyroCarePojo(in);
        }

        @Override
        public ThyroCarePojo[] newArray(int size) {
            return new ThyroCarePojo[size];
        }
    };

    public class Child
    {
        private String group_name = "";
        private String name = "";



        public String getName() { return name; }

        public void setName(String name) { this.name = name; }


        public String getGroup_name() { return this.group_name; }

        public void setGroup_name(String group_name) { this.group_name = group_name; }

        @Override
        public String toString() {
            return "{" +
                    "group_name='" + group_name + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
