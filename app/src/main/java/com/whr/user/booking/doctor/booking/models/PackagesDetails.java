package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackagesDetails implements Parcelable {
    private String PackageName;

    private String stay;

    private String Included;

    private String Excluded;

    private String Terms;

    @SerializedName("PerticularDetails")
    private List<PerticularDetails> perticularDetails;

    private String totaltreatmentfee;

    private String discountprice;

    private String hospname;

    private String address;

    private String profile_img;

    private String hid;

    private String tid;

    protected PackagesDetails(Parcel in) {
        PackageName = in.readString();
        stay = in.readString();
        Included = in.readString();
        Excluded = in.readString();
        Terms = in.readString();
        perticularDetails = in.createTypedArrayList(PerticularDetails.CREATOR);
        totaltreatmentfee = in.readString();
        discountprice = in.readString();
        hospname = in.readString();
        address = in.readString();
        profile_img = in.readString();
        hid = in.readString();
        tid = in.readString();
    }

    public static final Creator<PackagesDetails> CREATOR = new Creator<PackagesDetails>() {
        @Override
        public PackagesDetails createFromParcel(Parcel in) {
            return new PackagesDetails(in);
        }

        @Override
        public PackagesDetails[] newArray(int size) {
            return new PackagesDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(PackageName);
        parcel.writeString(stay);
        parcel.writeString(Included);
        parcel.writeString(Excluded);
        parcel.writeString(Terms);
        parcel.writeTypedList(perticularDetails);
        parcel.writeString(totaltreatmentfee);
        parcel.writeString(discountprice);
        parcel.writeString(hospname);
        parcel.writeString(address);
        parcel.writeString(profile_img);
        parcel.writeString(hid);
        parcel.writeString(tid);
    }

    public static class PerticularDetails implements Parcelable {

        protected PerticularDetails(Parcel in) {
            perticular = in.readString();
            fee = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(perticular);
            dest.writeString(fee);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PerticularDetails> CREATOR = new Creator<PerticularDetails>() {
            @Override
            public PerticularDetails createFromParcel(Parcel in) {
                return new PerticularDetails(in);
            }

            @Override
            public PerticularDetails[] newArray(int size) {
                return new PerticularDetails[size];
            }
        };

        /*@Override
        public String toString() {
            return "PerticularDetails{" +
                    "perticular='" + perticular + '\'' +
                    ", fee='" + fee + '\'' +
                    '}';
        }*/

        private String perticular;

        private String fee;

        public PerticularDetails(String perticular, String fee) {
            this.perticular = perticular;
            this.fee = fee;
        }

        public String getPerticular() {
            return perticular;
        }

        public String getFee() {
            return fee;
        }
    }
/*
    @Override
    public String toString() {
        return "PackagesDetails{" +
                "PackageName='" + PackageName + '\'' +
                ", stay='" + stay + '\'' +
                ", Included='" + Included + '\'' +
                ", Excluded='" + Excluded + '\'' +
                ", Terms='" + Terms + '\'' +
                ", perticularDetails=" + perticularDetails +
                ", totaltreatmentfee='" + totaltreatmentfee + '\'' +
                ", discountprice='" + discountprice + '\'' +
                ", hospname='" + hospname + '\'' +
                ", address='" + address + '\'' +
                ", profile_img='" + profile_img + '\'' +
                ", hid='" + hid + '\'' +
                ", tid='" + tid + '\'' +
                '}';
    }*/


    @Override
    public String toString() {
        return "PackagesDetails{" +
                "PackageName='" + PackageName + '\'' +
                ", totaltreatmentfee='" + totaltreatmentfee + '\'' +
                ", discountprice='" + discountprice + '\'' +
                ", hospname='" + hospname + '\'' +
                ", address='" + address + '\'' +
                ", profile_img='" + profile_img + '\'' +
                ", hid='" + hid + '\'' +
                ", tid='" + tid + '\'' +
                '}';
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getStay() {
        return stay;
    }

    public void setStay(String stay) {
        this.stay = stay;
    }

    public String getIncluded() {
        return Included;
    }

    public void setIncluded(String included) {
        Included = included;
    }

    public String getExcluded() {
        return Excluded;
    }

    public void setExcluded(String excluded) {
        Excluded = excluded;
    }

    public String getTerms() {
        return Terms;
    }

    public void setTerms(String terms) {
        Terms = terms;
    }

    public List<PerticularDetails> getPerticularDetails() {
        return perticularDetails;
    }

    public void setPerticularDetails(List<PerticularDetails> perticularDetails) {
        this.perticularDetails = perticularDetails;
    }

    public String getTotaltreatmentfee() {
        return totaltreatmentfee;
    }

    public void setTotaltreatmentfee(String totaltreatmentfee) {
        this.totaltreatmentfee = totaltreatmentfee;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
