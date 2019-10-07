package com.whr.user.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Etech001 on 18/11/2016.
 */

public class Suggested_TreatementPojo implements Parcelable {

    String date;
    String doctorName;
    String suggestTretTest;
    String suggestDate;
    String doctorAddress;
    String tname;
    double fee;
    int tid;
    int id;
    boolean isCheck;
    boolean isChecked;
    boolean ischeck;
    int rowid;
    String whr_discount, pathology_discount, whrdiscounted_price, pathology_discount_price;
    String description;

    public boolean isIscheck() {
        return ischeck;
    }

    public static Creator<Suggested_TreatementPojo> getCREATOR() {
        return CREATOR;
    }

    public Suggested_TreatementPojo(Parcel in) {

        date = in.readString();
        doctorName = in.readString();
        suggestTretTest = in.readString();
        suggestDate = in.readString();
        doctorAddress = in.readString();
        tname = in.readString();

        whr_discount = in.readString();
        pathology_discount = in.readString();
        whrdiscounted_price = in.readString();
        pathology_discount_price = in.readString();

        fee = in.readDouble();
        isChecked = in.readByte() != 0;
        ischeck = in.readByte() != 0;
        tid = in.readInt();
        id = in.readInt();
        description = in.readString();
        rowid = in.readInt();


    }

    public static final Creator<Suggested_TreatementPojo> CREATOR = new Creator<Suggested_TreatementPojo>() {
        @Override
        public Suggested_TreatementPojo createFromParcel(Parcel in) {
            return new Suggested_TreatementPojo(in);
        }

        @Override
        public Suggested_TreatementPojo[] newArray(int size) {
            return new Suggested_TreatementPojo[size];
        }
    };

    public Suggested_TreatementPojo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSuggestTretTest() {
        return suggestTretTest;
    }

    public void setSuggestTretTest(String suggestTretTest) {
        this.suggestTretTest = suggestTretTest;
    }

    public String getSuggestDate() {
        return suggestDate;
    }

    public void setSuggestDate(String suggestDate) {
        this.suggestDate = suggestDate;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

        public String getWhr_discount() {
        return whr_discount;
    }

    public void setWhr_discount(String whr_discount) {
        this.whr_discount = whr_discount;
    }

    public String getPathology_discount() {
        return pathology_discount;
    }

    public void setPathology_discount(String pathology_discount) {
        this.pathology_discount = pathology_discount;
    }

    public String getWhrdiscounted_price() {
        return whrdiscounted_price;
    }

    public void setWhrdiscounted_price(String whrdiscounted_price) {
        this.whrdiscounted_price = whrdiscounted_price;
    }

    public String getPathology_discount_price() {
        return pathology_discount_price;
    }

    public void setPathology_discount_price(String pathology_discount_price) {
        this.pathology_discount_price = pathology_discount_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(doctorName);
        dest.writeString(suggestTretTest);
        dest.writeString(suggestDate);
        dest.writeString(doctorAddress);
        dest.writeString(tname);
        dest.writeString(whr_discount);
        dest.writeString(pathology_discount);
        dest.writeString(whrdiscounted_price);
        dest.writeString(pathology_discount_price);
        dest.writeDouble(fee);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (ischeck ? 1 : 0));
        dest.writeInt(tid);
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeInt(rowid);
    }
}