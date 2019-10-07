package com.whr.user.booking.diagnostics.booking.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PathologyListPojo implements Parcelable {
    String id;
    String PathologyName;
    String address;
    String likes;
    String reviews;
    String profile_img;
    String type = "";
    double latitude;
    double longitude;
    double distance;
    String workingfrom;
    String workingto;
    String availabletestName = "";
    String remainingcount;
    String availablepackege;


    @SerializedName("likeStatus")
    boolean likeStatus = false;
    @SerializedName("favoriteStatus")
    boolean favoriteStatus = false;
    @SerializedName("testname")
    private ArrayList<testname> testnames = new ArrayList<>();

    protected PathologyListPojo(Parcel in) {
        id = in.readString();
        PathologyName = in.readString();
        address = in.readString();
        likes = in.readString();
        reviews = in.readString();
        profile_img = in.readString();
        type = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
        workingfrom = in.readString();
        workingto = in.readString();
        availabletestName = in.readString();
        remainingcount = in.readString();
        likeStatus = in.readByte() != 0;
        favoriteStatus = in.readByte() != 0;
        testnames = in.createTypedArrayList(testname.CREATOR);
    }

    public static final Creator<PathologyListPojo> CREATOR = new Creator<PathologyListPojo>() {
        @Override
        public PathologyListPojo createFromParcel(Parcel in) {
            return new PathologyListPojo(in);
        }

        @Override
        public PathologyListPojo[] newArray(int size) {
            return new PathologyListPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(PathologyName);
        dest.writeString(address);
        dest.writeString(likes);
        dest.writeString(reviews);
        dest.writeString(profile_img);
        dest.writeString(type);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(distance);
        dest.writeString(workingfrom);
        dest.writeString(workingto);
        dest.writeString(availabletestName);
        dest.writeString(remainingcount);
        dest.writeByte((byte) (likeStatus ? 1 : 0));
        dest.writeByte((byte) (favoriteStatus ? 1 : 0));
        dest.writeTypedList(testnames);
    }


    public static class testname implements Parcelable {
        String Testname = "";

        public String getTestname() {
            return Testname;
        }

        public void setTestname(String testname) {
            Testname = testname;
        }

        public static Creator<testname> getCREATOR() {
            return CREATOR;
        }

        protected testname(Parcel in) {
            Testname = in.readString();
        }

        public static final Creator<testname> CREATOR = new Creator<testname>() {
            @Override
            public testname createFromParcel(Parcel in) {
                return new testname(in);
            }

            @Override
            public testname[] newArray(int size) {
                return new testname[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(Testname);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathologyName() {
        return PathologyName;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getWorkingfrom() {
        return workingfrom;
    }

    public String getWorkingto() {
        return workingto;
    }

    public String getAvailabletestName() {
        return availabletestName;
    }

    public void setAvailabletestName(String availabletestName) {
        this.availabletestName = availabletestName;
    }

    public String getRemainingcount() {
        return remainingcount;
    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public boolean isFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public String getAvailablepackege() {
        return availablepackege;
    }

    public void setAvailablepackege(String availablepackege) {
        this.availablepackege = availablepackege;
    }

    public ArrayList<testname> getTestnames() {
        return testnames;
    }

    public static Creator<PathologyListPojo> getCREATOR() {
        return CREATOR;
    }
}
