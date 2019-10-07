package com.whr.user.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class FamilyMemberListpojo implements Parcelable {

    String familyName;
    String cno = "";
    long familyId;
    String dob = "";

    public FamilyMemberListpojo() {
    }



    public String toString1() {
        return "FamilyMemberListpojo{" +
                "familyName='" + familyName + '\'' +
                ", cno='" + cno + '\'' +
                ", familyId=" + familyId +
                ", dob='" + dob + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return familyName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(long familyId) {
        this.familyId = familyId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public static Creator<FamilyMemberListpojo> getCREATOR() {
        return CREATOR;
    }

    protected FamilyMemberListpojo(Parcel in) {
        familyName = in.readString();
        cno = in.readString();
        familyId = in.readLong();
        dob = in.readString();
    }

    public static final Creator<FamilyMemberListpojo> CREATOR = new Creator<FamilyMemberListpojo>() {
        @Override
        public FamilyMemberListpojo createFromParcel(Parcel in) {
            return new FamilyMemberListpojo(in);
        }

        @Override
        public FamilyMemberListpojo[] newArray(int size) {
            return new FamilyMemberListpojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(familyName);
        parcel.writeString(cno);
        parcel.writeLong(familyId);
        parcel.writeString(dob);
    }


}
