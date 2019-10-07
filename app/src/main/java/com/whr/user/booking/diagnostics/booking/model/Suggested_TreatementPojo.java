package com.whr.user.booking.diagnostics.booking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Etech001 on 18/11/2016.
 */

public class Suggested_TreatementPojo implements Parcelable {

    String tid;
    String rowid;
    String tname;
    String fee;
    String whr_discount;
    String pathology_discount;
    String whrdiscounted_price;
    String description;
    boolean isCheck = false;


    @Override
    public String toString() {
        return "Suggested_TreatementPojo{" +
                "tid='" + tid + '\'' +
                ", rowid='" + rowid + '\'' +
                ", tname='" + tname + '\'' +
                ", fee='" + fee + '\'' +
                ", whr_discount='" + whr_discount + '\'' +
                ", pathology_discount='" + pathology_discount + '\'' +
                ", whrdiscounted_price='" + whrdiscounted_price + '\'' +
                ", description='" + description + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean checked) {
        isCheck = checked;
    }


    public static Creator<Suggested_TreatementPojo> getCREATOR() {
        return CREATOR;
    }

    protected Suggested_TreatementPojo(Parcel in) {
        tid = in.readString();
        rowid = in.readString();
        tname = in.readString();
        fee = in.readString();
        whr_discount = in.readString();
        pathology_discount = in.readString();
        whrdiscounted_price = in.readString();
        description = in.readString();
        isCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tid);
        dest.writeString(rowid);
        dest.writeString(tname);
        dest.writeString(fee);
        dest.writeString(whr_discount);
        dest.writeString(pathology_discount);
        dest.writeString(whrdiscounted_price);
        dest.writeString(description);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
}