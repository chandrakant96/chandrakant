package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DoctorModel implements Parcelable {


    private String type = "";

    private int id = 0;

    private String name = "";

    private String education = "";

    private String special = "";

    private String experience = "";

    @SerializedName("Clinicname")
    private ArrayList<Clinicname> clinicname = new ArrayList<>();

    private String fee = "";

    private double distance = 0.0;

    private int likes = 0;

    private int totalDist = 0;

    private int reviews = 0;

    private String whrgivendiscount = "0";

    private String whrdiscountedfee = "0";

    private String profile_img = "";

    private double latitude = 0.0;

    private double longitude = 0.0;

    private boolean availablestatus = false;

    private String specializationId = "0";

    public DoctorModel() {

    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fee='" + fee + '\'' +
                ", whrdiscountedfee='" + whrdiscountedfee + '\'' +
                '}';
    }

    protected DoctorModel(Parcel in) {
        type = in.readString();
        id = in.readInt();
        name = in.readString();
        education = in.readString();
        special = in.readString();
        experience = in.readString();
        clinicname = in.createTypedArrayList(Clinicname.CREATOR);
        fee = in.readString();
        distance = in.readDouble();
        likes = in.readInt();
        totalDist = in.readInt();
        reviews = in.readInt();
        whrgivendiscount = in.readString();
        whrdiscountedfee = in.readString();
        profile_img = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        availablestatus = in.readByte() != 0;
        specializationId = in.readString();
    }

    public static final Creator<DoctorModel> CREATOR = new Creator<DoctorModel>() {
        @Override
        public DoctorModel createFromParcel(Parcel in) {
            return new DoctorModel(in);
        }

        @Override
        public DoctorModel[] newArray(int size) {
            return new DoctorModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(education);
        dest.writeString(special);
        dest.writeString(experience);
        dest.writeTypedList(clinicname);
        dest.writeString(fee);
        dest.writeDouble(distance);
        dest.writeInt(likes);
        dest.writeInt(totalDist);
        dest.writeInt(reviews);
        dest.writeString(whrgivendiscount);
        dest.writeString(whrdiscountedfee);
        dest.writeString(profile_img);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (availablestatus ? 1 : 0));
        dest.writeString(specializationId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public ArrayList<Clinicname> getClinicname() {
        return clinicname;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public String getWhrdiscountedfee() {
        return whrdiscountedfee;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
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

    public boolean isAvailablestatus() {
        return availablestatus;
    }

    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    public static Creator<DoctorModel> getCREATOR() {
        return CREATOR;
    }
}