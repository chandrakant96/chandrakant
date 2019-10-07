package com.whr.user.booking.packages.hospital.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class SurgeryPackageInHospitals implements Parcelable {


    private String hospitalname;

    private String address;

    private String distance;

    private String minfee;

    private String maxfee;

    private int hid;

    private double lat;

    private double log;

    private List<Clinicimages> clinicimages;

    protected SurgeryPackageInHospitals(Parcel in) {
        hospitalname = in.readString();
        address = in.readString();
        distance = in.readString();
        minfee = in.readString();
        maxfee = in.readString();
        hid = in.readInt();
        lat = in.readDouble();
        log = in.readDouble();
        clinicimages = in.createTypedArrayList(Clinicimages.CREATOR);
    }

    public static final Creator<SurgeryPackageInHospitals> CREATOR = new Creator<SurgeryPackageInHospitals>() {
        @Override
        public SurgeryPackageInHospitals createFromParcel(Parcel in) {
            return new SurgeryPackageInHospitals(in);
        }

        @Override
        public SurgeryPackageInHospitals[] newArray(int size) {
            return new SurgeryPackageInHospitals[size];
        }
    };

    public List<Clinicimages> getClinicimages() {
        return clinicimages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hospitalname);
        dest.writeString(address);
        dest.writeString(distance);
        dest.writeString(minfee);
        dest.writeString(maxfee);
        dest.writeInt(hid);
        dest.writeDouble(lat);
        dest.writeDouble(log);
        dest.writeTypedList(clinicimages);
    }

    public static class Clinicimages implements Parcelable {

        private String imagespath;

        public String getImagespath() {
            return imagespath;
        }

        public void setImagespath(String imagespath) {
            this.imagespath = imagespath;
        }

        public static Creator<Clinicimages> getCREATOR() {
            return CREATOR;
        }

        protected Clinicimages(Parcel in) {
            imagespath = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imagespath);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Clinicimages> CREATOR = new Creator<Clinicimages>() {
            @Override
            public Clinicimages createFromParcel(Parcel in) {
                return new Clinicimages(in);
            }

            @Override
            public Clinicimages[] newArray(int size) {
                return new Clinicimages[size];
            }
        };

        @Override
        public String toString() {
            return "Clinicimages{" +
                    "imagespath='" + imagespath + '\'' +
                    '}';
        }
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMinfee() {
        return minfee;
    }

    public void setMinfee(String minfee) {
        this.minfee = minfee;
    }

    public String getMaxfee() {
        return maxfee;
    }

    public void setMaxfee(String maxfee) {
        this.maxfee = maxfee;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public void setClinicimages(List<Clinicimages> clinicimages) {
        this.clinicimages = clinicimages;
    }

    public static Creator<SurgeryPackageInHospitals> getCREATOR() {
        return CREATOR;
    }
}
