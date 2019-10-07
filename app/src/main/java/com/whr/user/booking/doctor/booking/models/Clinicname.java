package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Clinicname implements Parcelable {
    String hid = "";
    String clinicname = "";

    @NonNull
    @Override
    public String toString() {
        return "Clinicname{" +
                "hid='" + hid + '\'' +
                ", clinicname='" + clinicname + '\'' +
                '}';
    }

    protected Clinicname(Parcel in) {
        hid = in.readString();
        clinicname = in.readString();
    }


    public Clinicname() {
    }

    public Clinicname(String hid, String clinicname) {
        this.hid = hid;
        this.clinicname = clinicname;
    }

    public static final Creator<Clinicname> CREATOR = new Creator<Clinicname>() {
        @Override
        public Clinicname createFromParcel(Parcel in) {
            return new Clinicname(in);
        }

        @Override
        public Clinicname[] newArray(int size) {
            return new Clinicname[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hid);
        parcel.writeString(clinicname);
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getClinicname() {
        return clinicname;
    }

    public void setClinicname(String clinicname) {
        this.clinicname = clinicname;
    }

    public static Creator<Clinicname> getCREATOR() {
        return CREATOR;
    }
}
