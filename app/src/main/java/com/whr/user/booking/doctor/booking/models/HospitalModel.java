package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HospitalModel implements Parcelable {
    private String specialization = "0";

    private int id;

    private String HospitalName;

    private double distance = 0.0;

    private int likes;

    private int totalDist;


    private int reviews;

    @SerializedName("profile_img")
    private String profileImg;

    private int availablepackege;

    private int availabledoctor;

    private double latitude = 0.0;

    private double longitude = 0.0;

    private String type = "";

    private String packdiscount = "";

    private String bedcount = "0";

    public String Minpackege = "";

    public HospitalModel() {

    }

    protected HospitalModel(Parcel in) {
        specialization = in.readString();
        id = in.readInt();
        HospitalName = in.readString();
        distance = in.readDouble();
        likes = in.readInt();
        totalDist = in.readInt();
        reviews = in.readInt();
        profileImg = in.readString();
        availablepackege = in.readInt();
        availabledoctor = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        type = in.readString();
        packdiscount = in.readString();
        bedcount = in.readString();
        Minpackege = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(specialization);
        dest.writeInt(id);
        dest.writeString(HospitalName);
        dest.writeDouble(distance);
        dest.writeInt(likes);
        dest.writeInt(totalDist);
        dest.writeInt(reviews);
        dest.writeString(profileImg);
        dest.writeInt(availablepackege);
        dest.writeInt(availabledoctor);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(type);
        dest.writeString(packdiscount);
        dest.writeString(bedcount);
        dest.writeString(Minpackege);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HospitalModel> CREATOR = new Creator<HospitalModel>() {
        @Override
        public HospitalModel createFromParcel(Parcel in) {
            return new HospitalModel(in);
        }

        @Override
        public HospitalModel[] newArray(int size) {
            return new HospitalModel[size];
        }
    };

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public int getAvailablepackege() {
        return availablepackege;
    }

    public int getAvailabledoctor() {
        return availabledoctor;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackdiscount() {
        return packdiscount;
    }

    public String getBedcount() {
        return bedcount;
    }

    public String getMinpackege() {
        return Minpackege;
    }

    public static Creator<HospitalModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "HospitalModel{" +
                "specialization='" + specialization + '\'' +
                ", id=" + id +
                ", HospitalName='" + HospitalName + '\'' +
                ", distance=" + distance +
                ", likes=" + likes +
                ", totalDist=" + totalDist +
                ", reviews=" + reviews +
                ", profileImg='" + profileImg + '\'' +
                ", availablepackege=" + availablepackege +
                ", availabledoctor=" + availabledoctor +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type='" + type + '\'' +
                ", packdiscount='" + packdiscount + '\'' +
                ", bedcount='" + bedcount + '\'' +
                ", Minpackege='" + Minpackege + '\'' +
                '}';
    }
}