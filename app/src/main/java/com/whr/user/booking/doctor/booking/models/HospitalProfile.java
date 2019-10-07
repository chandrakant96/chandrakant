package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HospitalProfile {

    @Override
    public String toString() {
        return "HospitalProfile{" +
                "doctorList=" + doctorList +
                ", reviews=" + reviews +
                ", hospitalInfo=" + hospitalInfo +
                ", hospitalPackages=" + hospitalPackages +
                '}';
    }

    public void setDoctorList(List<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    public void setHospitalInfo(HospitalInfo hospitalInfo) {
        this.hospitalInfo = hospitalInfo;
    }

    public void setHospitalPackages(List<HospitalPackages> hospitalPackages) {
        this.hospitalPackages = hospitalPackages;
    }

    private List<DoctorList> doctorList;

    private List<Reviews> reviews;

    private HospitalInfo hospitalInfo;

    private List<HospitalPackages> hospitalPackages;

    public HospitalProfile(List<DoctorList> doctorList, List<Reviews> reviews,
                           HospitalInfo hospitalInfo, List<HospitalPackages> hospitalPackages) {
        this.doctorList = doctorList;
        this.reviews = reviews;
        this.hospitalInfo = hospitalInfo;
        this.hospitalPackages = hospitalPackages;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public static class DoctorList implements Parcelable {

        private String name;

        private String education;

        private String experience;

        private String special;

        private String profileimage;

        private int likes;

        private boolean likeStatus;

        private boolean favoriteStatus;

        private boolean availablestatus;

        private String whrgivendiscount;

        private String fee;

        private double whrdiscountedfee;

        private String did = "";

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

        public boolean isAvailablestatus() {
            return availablestatus;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public double getWhrdiscountedfee() {
            return whrdiscountedfee;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public static Creator<DoctorList> getCREATOR() {
            return CREATOR;
        }

        protected DoctorList(Parcel in) {
            name = in.readString();
            education = in.readString();
            experience = in.readString();
            special = in.readString();
            profileimage = in.readString();
            likes = in.readInt();
            likeStatus = in.readByte() != 0;
            favoriteStatus = in.readByte() != 0;
            availablestatus = in.readByte() != 0;
            whrgivendiscount = in.readString();
            fee = in.readString();
            whrdiscountedfee = in.readDouble();
            did = in.readString();
        }

        public static final Creator<DoctorList> CREATOR = new Creator<DoctorList>() {
            @Override
            public DoctorList createFromParcel(Parcel in) {
                return new DoctorList(in);
            }

            @Override
            public DoctorList[] newArray(int size) {
                return new DoctorList[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(education);
            parcel.writeString(experience);
            parcel.writeString(special);
            parcel.writeString(profileimage);
            parcel.writeInt(likes);
            parcel.writeByte((byte) (likeStatus ? 1 : 0));
            parcel.writeByte((byte) (favoriteStatus ? 1 : 0));
            parcel.writeByte((byte) (availablestatus ? 1 : 0));
            parcel.writeString(whrgivendiscount);
            parcel.writeString(fee);
            parcel.writeDouble(whrdiscountedfee);
            parcel.writeString(did);
        }
    }

    public static class Reviews {
        private String username;

        @Override
        public String toString() {
            return "Reviews{" +
                    "username='" + username + '\'' +
                    ", userreview='" + userreview + '\'' +
                    '}';
        }

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

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class HospitalInfo implements Parcelable {

        protected HospitalInfo(Parcel in) {
            hospitalname = in.readString();
            address = in.readString();
            doctorcount = in.readInt();
            profileimage = in.readString();
            latitude = in.readDouble();
            longitude = in.readDouble();
            likes = in.readInt();
            likeStatus = in.readByte() != 0;
            favoriteStatus = in.readByte() != 0;
            distance = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(hospitalname);
            dest.writeString(address);
            dest.writeInt(doctorcount);
            dest.writeString(profileimage);
            dest.writeDouble(latitude);
            dest.writeDouble(longitude);
            dest.writeInt(likes);
            dest.writeByte((byte) (likeStatus ? 1 : 0));
            dest.writeByte((byte) (favoriteStatus ? 1 : 0));
            dest.writeDouble(distance);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<HospitalInfo> CREATOR = new Creator<HospitalInfo>() {
            @Override
            public HospitalInfo createFromParcel(Parcel in) {
                return new HospitalInfo(in);
            }

            @Override
            public HospitalInfo[] newArray(int size) {
                return new HospitalInfo[size];
            }
        };

        public void setHospitalname(String hospitalname) {
            this.hospitalname = hospitalname;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public void setLikeStatus(boolean likeStatus) {
            this.likeStatus = likeStatus;
        }

        public void setFavoriteStatus(boolean favoriteStatus) {
            this.favoriteStatus = favoriteStatus;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }


        @SerializedName("hospitalname")
        private String hospitalname;

        @SerializedName("address")
        private String address;

        @SerializedName("bedcount")
        private Object bedcount;

        @SerializedName("doctorcount")
        private int doctorcount;

        @SerializedName("profileimage")
        private String profileimage;

        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        @SerializedName("establishedin")
        private Object establishedin;

        @SerializedName("likes")
        private int likes;

        @SerializedName("likeStatus")
        private boolean likeStatus;

        @SerializedName("favoriteStatus")
        private boolean favoriteStatus;

        @SerializedName("distance")
        private double distance;

        @SerializedName("clinicimages")
        private List<Clinicimages> clinicimages;

        public String getHospitalname() {
            return hospitalname;
        }

        public String getAddress() {
            return address;
        }

        public Object getBedcount() {
            return bedcount;
        }

        public int getDoctorcount() {
            return doctorcount;
        }

        public String getProfileimage() {
            return profileimage;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getLikes() {
            return likes;
        }

        public boolean isLikeStatus() {
            return likeStatus;
        }

        public boolean isFavoriteStatus() {
            return favoriteStatus;
        }

        public double getDistance() {
            return distance;
        }

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

        @Override
        public String toString() {
            return "HospitalInfo{" +
                    "hospitalname='" + hospitalname + '\'' +
                    ", address='" + address + '\'' +
                    ", bedcount=" + bedcount +
                    ", doctorcount=" + doctorcount +
                    ", profileimage='" + profileimage + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", establishedin=" + establishedin +
                    ", likes=" + likes +
                    ", likeStatus=" + likeStatus +
                    ", favoriteStatus=" + favoriteStatus +
                    ", distance=" + distance +
                    ", clinicimages=" + clinicimages +
                    '}';
        }
    }

    public static class HospitalPackages implements Parcelable {

        protected HospitalPackages(Parcel in) {
            id = in.readInt();
            packagename = in.readString();
            description = in.readString();
            packagesrelation = in.createTypedArrayList(Packagesrelation.CREATOR);
        }

        public static final Creator<HospitalPackages> CREATOR = new Creator<HospitalPackages>() {
            @Override
            public HospitalPackages createFromParcel(Parcel in) {
                return new HospitalPackages(in);
            }

            @Override
            public HospitalPackages[] newArray(int size) {
                return new HospitalPackages[size];
            }
        };

        @Override
        public String toString() {
            return "HospitalPackages{" +
                    "id=" + id +
                    ", packagename='" + packagename + '\'' +
                    ", packagesrelation=" + packagesrelation +
                    '}';
        }

        private int id;
        private String packagename;
        private String description;
        private List<Packagesrelation> packagesrelation;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPackagename() {
            return packagename;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Packagesrelation> getPackagesrelation() {
            return packagesrelation;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(packagename);
            parcel.writeString(description);
            parcel.writeTypedList(packagesrelation);
        }

        public static class Packagesrelation implements Parcelable {

            @Override
            public String toString() {
                return "Packagesrelation{" +
                        "HospitalPackagetableSecondId=" + HospitalPackagetableSecondId +
                        ", whrdiscount=" + whrdiscount +
                        ", fee='" + fee + '\'' +
                        ", discount_price='" + discount_price + '\'' +
                        ", Ward='" + Ward + '\'' +
                        ", discount=" + discount +
                        ", isChecked=" + isChecked +
                        '}';
            }

            private int HospitalPackagetableSecondId;

            private int whrdiscount;

            private String fee;

            private String discount_price;

            private String Ward;

            private int discount;

            private boolean isChecked = false;

            public int getHospitalPackagetableSecondId() {
                return HospitalPackagetableSecondId;
            }

            public int getWhrdiscount() {
                return whrdiscount;
            }

            public void setWhrdiscount(int whrdiscount) {
                this.whrdiscount = whrdiscount;
            }

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }

            public String getDiscount_price() {
                return discount_price;
            }

            public String getWard() {
                return Ward;
            }

            public int getDiscount() {
                return discount;
            }

            public void setDiscount(int discount) {
                this.discount = discount;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public static Creator<Packagesrelation> getCREATOR() {
                return CREATOR;
            }

            protected Packagesrelation(Parcel in) {
                HospitalPackagetableSecondId = in.readInt();
                whrdiscount = in.readInt();
                fee = in.readString();
                discount_price = in.readString();
                Ward = in.readString();
                discount = in.readInt();
                isChecked = in.readByte() != 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(HospitalPackagetableSecondId);
                dest.writeInt(whrdiscount);
                dest.writeString(fee);
                dest.writeString(discount_price);
                dest.writeString(Ward);
                dest.writeInt(discount);
                dest.writeByte((byte) (isChecked ? 1 : 0));
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Packagesrelation> CREATOR = new Creator<Packagesrelation>() {
                @Override
                public Packagesrelation createFromParcel(Parcel in) {
                    return new Packagesrelation(in);
                }

                @Override
                public Packagesrelation[] newArray(int size) {
                    return new Packagesrelation[size];
                }
            };
        }
    }
}
