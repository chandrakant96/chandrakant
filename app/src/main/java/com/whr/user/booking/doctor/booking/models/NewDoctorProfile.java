package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewDoctorProfile {

    @SerializedName("HospitalProfile")
    private List<HospitalProfile> hospitalProfile;

    private List<Reviews> reviews;

    private DoctorInfo doctorInfo;

    public void setHospitalProfile(List<HospitalProfile> hospitalProfile) {
        this.hospitalProfile = hospitalProfile;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    public void setDoctorInfo(DoctorInfo doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    public NewDoctorProfile(List<HospitalProfile> hospitalProfile, List<Reviews> reviews,
                            DoctorInfo doctorInfo) {
        this.hospitalProfile = hospitalProfile;
        this.reviews = reviews;
        this.doctorInfo = doctorInfo;
    }

    public List<HospitalProfile> getHospitalProfile() {
        return hospitalProfile;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public DoctorInfo getDoctorInfo() {
        return doctorInfo;
    }

    public static class HospitalProfile implements Parcelable {

        String nextTimeSlots;

        String hid = "";

        private String hospname;

        private String address;

        private double latitude;

        private double longitude;

        private boolean availablestatus;

        private List<Clinicimages> clinicimages;

        @SerializedName("TimeSlots")
        private List<TimeSlots> timeSlots;

        private Object reviews;

        private String whrgivendiscount;

        private String fees;

        private double whrdiscountedfee;

        private Object doctorinfo;

        private double distance;

        String todaystime = "";

        protected HospitalProfile(Parcel in) {
            nextTimeSlots = in.readString();
            hid = in.readString();
            hospname = in.readString();
            address = in.readString();
            latitude = in.readDouble();
            longitude = in.readDouble();
            availablestatus = in.readByte() != 0;
            whrgivendiscount = in.readString();
            fees = in.readString();
            whrdiscountedfee = in.readDouble();
            distance = in.readDouble();
            todaystime = in.readString();
        }

        public static final Creator<HospitalProfile> CREATOR = new Creator<HospitalProfile>() {
            @Override
            public HospitalProfile createFromParcel(Parcel in) {
                return new HospitalProfile(in);
            }

            @Override
            public HospitalProfile[] newArray(int size) {
                return new HospitalProfile[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(nextTimeSlots);
            dest.writeString(hid);
            dest.writeString(hospname);
            dest.writeString(address);
            dest.writeDouble(latitude);
            dest.writeDouble(longitude);
            dest.writeByte((byte) (availablestatus ? 1 : 0));
            dest.writeString(whrgivendiscount);
            dest.writeString(fees);
            dest.writeDouble(whrdiscountedfee);
            dest.writeDouble(distance);
            dest.writeString(todaystime);
        }

        public static class Clinicimages {
            private String imagespath;

            public Clinicimages(String imagespath) {
                this.imagespath = imagespath;
            }

            public String getImagespath() {
                return imagespath;
            }
        }

        public static class TimeSlots {
            private String schedulestring;

            public String getSchedulestring() {
                return schedulestring;
            }

        }

        public String getNextTimeSlots() {
            return nextTimeSlots;
        }

        public void setNextTimeSlots(String nextTimeSlots) {
            this.nextTimeSlots = nextTimeSlots;
        }

        public String getHid() {
            return hid;
        }

        public void setHid(String hid) {
            this.hid = hid;
        }

        public String getHospname() {
            return hospname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public List<Clinicimages> getClinicimages() {
            return clinicimages;
        }

        public List<TimeSlots> getTimeSlots() {
            return timeSlots;
        }

        public Object getReviews() {
            return reviews;
        }

        public void setReviews(Object reviews) {
            this.reviews = reviews;
        }

        public String getFees() {
            return fees;
        }

        public void setFees(String fees) {
            this.fees = fees;
        }

        public double getWhrdiscountedfee() {
            return whrdiscountedfee;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getTodaystime() {
            return todaystime;
        }

        public static Creator<HospitalProfile> getCREATOR() {
            return CREATOR;
        }
    }

    public static class Reviews {
        @Override
        public String toString() {
            return "Reviews{" +
                    "username='" + username + '\'' +
                    ", userreview='" + userreview + '\'' +
                    '}';
        }

        private String username;

        private String userreview;

        public Reviews(String username, String userreview) {
            this.username = username;
            this.userreview = userreview;
        }

        public String getUsername() {
            return username;
        }

        public String getUserreview() {
            return userreview;
        }
    }

    public static class DoctorInfo {

        @Override
        public String toString() {
            return "DoctorInfo{" +
                    "name='" + name + '\'' +
                    ", education='" + education + '\'' +
                    ", experience='" + experience + '\'' +
                    ", special='" + special + '\'' +
                    ", profileimage='" + profileimage + '\'' +
                    ", sid='" + sid + '\'' +
                    ", likes=" + likes +
                    ", likeStatus=" + likeStatus +
                    ", favoriteStatus=" + favoriteStatus +
                    '}';
        }

        String name;
        String education;
        String experience;
        String special;
        String profileimage;
        String sid;
        int likes;
        boolean likeStatus;
        boolean favoriteStatus;

        @SerializedName("clinicimages")
        private List<Clinicimages> clinicimages;

        public List<Clinicimages> getClinicimages() {
            return clinicimages;
        }

        public static class Clinicimages {

            @Override
            public String toString() {
                return "Clinicimages{" +
                        "imagespath='" + imagespath + '\'' +
                        '}';
            }

            @SerializedName("imagespath")
            private String imagespath;

            public Clinicimages(String imagespath) {
                this.imagespath = imagespath;
            }

            public String getImagespath() {
                return imagespath;
            }
        }

        public String getSid() {
            return sid;
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

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getSpecial() {
            return special;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public String getProfileimage() {
            return profileimage;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
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
    }
}
