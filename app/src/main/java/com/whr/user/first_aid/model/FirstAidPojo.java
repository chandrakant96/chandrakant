package com.whr.user.first_aid.model;


import android.os.Parcel;
import android.os.Parcelable;

public class FirstAidPojo implements Parcelable {
    String VideoPath;
    String DoctorName;
    String FirstAidType;
    String id;



    protected FirstAidPojo(Parcel in) {
        VideoPath = in.readString();
        DoctorName = in.readString();
        FirstAidType = in.readString();
        id = in.readString();
    }

    public static final Creator<FirstAidPojo> CREATOR = new Creator<FirstAidPojo>() {
        @Override
        public FirstAidPojo createFromParcel(Parcel in) {
            return new FirstAidPojo(in);
        }

        @Override
        public FirstAidPojo[] newArray(int size) {
            return new FirstAidPojo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getFirstAidType() {
        return FirstAidType;
    }

    public void setFirstAidType(String firstAidType) {
        FirstAidType = firstAidType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VideoPath);
        dest.writeString(DoctorName);
        dest.writeString(FirstAidType);
        dest.writeString(id);
    }
}