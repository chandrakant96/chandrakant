package com.whr.user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewFavouritePojo implements Parcelable {

    int  id;
    String specid;
    String name;
    String specialization;
    String education;
    String doctorcount;
    String experience;
    String profile_img;
    String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecid() {
        return specid;
    }

    public void setSpecid(String specid) {
        this.specid = specid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDoctorcount() {
        return doctorcount;
    }

    public void setDoctorcount(String doctorcount) {
        this.doctorcount = doctorcount;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Creator<NewFavouritePojo> getCREATOR() {
        return CREATOR;
    }


    protected NewFavouritePojo(Parcel in) {
        id = in.readInt();
        specid = in.readString();
        name = in.readString();
        specialization = in.readString();
        education = in.readString();
        doctorcount = in.readString();
        experience = in.readString();
        profile_img = in.readString();
        type = in.readString();
    }

    public static final Creator<NewFavouritePojo> CREATOR = new Creator<NewFavouritePojo>() {
        @Override
        public NewFavouritePojo createFromParcel(Parcel in) {
            return new NewFavouritePojo(in);
        }

        @Override
        public NewFavouritePojo[] newArray(int size) {
            return new NewFavouritePojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(specid);
        dest.writeString(name);
        dest.writeString(specialization);
        dest.writeString(education);
        dest.writeString(doctorcount);
        dest.writeString(experience);
        dest.writeString(profile_img);
        dest.writeString(type);
    }
}
