package com.whr.user.booking.packages.hospital.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SurgeryPackagesModel implements Parcelable {

    public SurgeryPackagesModel() {

    }

    public void setPckname(String pckname) {
        this.pckname = pckname;
    }

    public void setMinfee(String minfee) {
        this.minfee = minfee;
    }

    public void setMaxfee(String maxfee) {
        this.maxfee = maxfee;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPackid(int packid) {
        this.packid = packid;
    }

    public void setHospitals(int hospitals) {
        this.hospitals = hospitals;
    }

    public void setDoctors(int doctors) {
        this.doctors = doctors;
    }

    public void setPackagesrelation(List<Packagesrelation> packagesrelation) {
        this.packagesrelation = packagesrelation;
    }

    public static Creator<SurgeryPackagesModel> getCREATOR() {
        return CREATOR;
    }

    public static void setCREATOR(Creator<SurgeryPackagesModel> CREATOR) {
        SurgeryPackagesModel.CREATOR = CREATOR;
    }

    private String pckname;

    private String minfee;

    private String maxfee;

    private String description;

    private int packid;

    private int hospitals;

    private int doctors;

    private List<Packagesrelation> packagesrelation;

    @Override
    public String toString() {
        return "SurgeryPackagesModel{" +
                "pckname='" + pckname + '\'' +
                ", minfee='" + minfee + '\'' +
                ", maxfee='" + maxfee + '\'' +
                ", description='" + description + '\'' +
                ", packid=" + packid +
                ", hospitals=" + hospitals +
                ", doctors=" + doctors +
                ", packagesrelation=" + packagesrelation +
                '}';
    }

    public SurgeryPackagesModel(String pckname, String minfee, String maxfee, String description,
                                int packid, int hospitals, int doctors, List<Packagesrelation> packagesrelation) {
        this.pckname = pckname;
        this.minfee = minfee;
        this.maxfee = maxfee;
        this.description = description;
        this.packid = packid;
        this.hospitals = hospitals;
        this.doctors = doctors;
        this.packagesrelation = packagesrelation;
    }

    protected SurgeryPackagesModel(Parcel in) {
        pckname = in.readString();
        minfee = in.readString();
        maxfee = in.readString();
        description = in.readString();
        packid = in.readInt();
        hospitals = in.readInt();
        doctors = in.readInt();
        packagesrelation = in.createTypedArrayList(Packagesrelation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pckname);
        dest.writeString(minfee);
        dest.writeString(maxfee);
        dest.writeString(description);
        dest.writeInt(packid);
        dest.writeInt(hospitals);
        dest.writeInt(doctors);
        dest.writeTypedList(packagesrelation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Creator<SurgeryPackagesModel> CREATOR = new Creator<SurgeryPackagesModel>() {
        @Override
        public SurgeryPackagesModel createFromParcel(Parcel in) {
            return new SurgeryPackagesModel(in);
        }

        @Override
        public SurgeryPackagesModel[] newArray(int size) {
            return new SurgeryPackagesModel[size];
        }
    };

    public String getPckname() {
        return pckname;
    }

    public String getMinfee() {
        return minfee;
    }

    public String getMaxfee() {
        return maxfee;
    }

    public String getDescription() {
        return description;
    }

    public int getPackid() {
        return packid;
    }

    public int getHospitals() {
        return hospitals;
    }

    public int getDoctors() {
        return doctors;
    }

    public List<Packagesrelation> getPackagesrelation() {
        return packagesrelation;
    }

    public static class Packagesrelation implements Parcelable {

        public void setHospitalPackagetableSecondId(int hospitalPackagetableSecondId) {
            this.hospitalPackagetableSecondId = hospitalPackagetableSecondId;
        }

        public void setWhrdiscount(int whrdiscount) {
            this.whrdiscount = whrdiscount;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public static Creator<Packagesrelation> getCREATOR() {
            return CREATOR;
        }

        public static void setCREATOR(Creator<Packagesrelation> CREATOR) {
            Packagesrelation.CREATOR = CREATOR;
        }

        @Override
        public String toString() {
            return "Packagesrelation{" +
                    "hospitalPackagetableSecondId=" + hospitalPackagetableSecondId +
                    ", whrdiscount=" + whrdiscount +
                    ", fee='" + fee + '\'' +
                    ", discountPrice='" + discountPrice + '\'' +
                    ", ward='" + ward + '\'' +
                    ", discount=" + discount +
                    '}';
        }

        @SerializedName("HospitalPackagetableSecondId")
        private int hospitalPackagetableSecondId;

        private int whrdiscount;

        private String fee;

        @SerializedName("discount_price")
        private String discountPrice;

        @SerializedName("Ward")
        private String ward;

        private int discount;

        public Packagesrelation(int hospitalPackagetableSecondId, int whrdiscount, String fee,
                                String discountPrice, String ward, int discount) {
            this.hospitalPackagetableSecondId = hospitalPackagetableSecondId;
            this.whrdiscount = whrdiscount;
            this.fee = fee;
            this.discountPrice = discountPrice;
            this.ward = ward;
            this.discount = discount;
        }

        protected Packagesrelation(Parcel in) {
            hospitalPackagetableSecondId = in.readInt();
            whrdiscount = in.readInt();
            fee = in.readString();
            discountPrice = in.readString();
            ward = in.readString();
            discount = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(hospitalPackagetableSecondId);
            dest.writeInt(whrdiscount);
            dest.writeString(fee);
            dest.writeString(discountPrice);
            dest.writeString(ward);
            dest.writeInt(discount);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static Creator<Packagesrelation> CREATOR = new Creator<Packagesrelation>() {
            @Override
            public Packagesrelation createFromParcel(Parcel in) {
                return new Packagesrelation(in);
            }

            @Override
            public Packagesrelation[] newArray(int size) {
                return new Packagesrelation[size];
            }
        };

        public int getHospitalPackagetableSecondId() {
            return hospitalPackagetableSecondId;
        }

        public int getWhrdiscount() {
            return whrdiscount;
        }

        public String getFee() {
            return fee;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public String getWard() {
            return ward;
        }

        public int getDiscount() {
            return discount;
        }
    }
}
