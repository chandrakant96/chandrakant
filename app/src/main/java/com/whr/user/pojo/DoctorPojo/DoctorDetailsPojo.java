package com.whr.user.pojo.DoctorPojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 12/1/2016.
 */

public class DoctorDetailsPojo implements Parcelable {


    public DoctorDetailsPojo() {
    }

    private int id;
    private int sid;
    private String name;
    private String addr;
    private String cno;
    private String special;


    private int specialid;
    private String images;
    private int totalDist;
    private String profile_img;
    private String pathy;

    private Double latitute;
    private Double longitute;

    private String address;
    private ArrayList<Clinicimages> clinicimages;

    private String education;
    private String experience;
    private String delivery;
    private int reviews;
    private int quata;
    private String fee = "";
    private Double distance;
    private int likes;
    private boolean likeStatus;
    private boolean favoriteStatus;
    private String PersonalHospital;
    private String insurance;
    private String testname;
    private String testfee;
    private String availablepackege = " ";

    boolean isCheck;
    boolean isChecked;
    boolean ischeck;

    String address1 = "";
    String latitute1 = "0.0";
    String longitute1 = "0.0";

    String whrgivendiscount = "";

    String whrdiscountedfee = "";

    protected DoctorDetailsPojo(Parcel in) {
        id = in.readInt();
        sid = in.readInt();
        name = in.readString();
        addr = in.readString();
        cno = in.readString();
        special = in.readString();
        specialid = in.readInt();
        images = in.readString();
        totalDist = in.readInt();
        profile_img = in.readString();
        pathy = in.readString();
        if (in.readByte() == 0) {
            latitute = null;
        } else {
            latitute = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitute = null;
        } else {
            longitute = in.readDouble();
        }
        address = in.readString();
        education = in.readString();
        experience = in.readString();
        delivery = in.readString();
        reviews = in.readInt();
        quata = in.readInt();
        fee = in.readString();
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readDouble();
        }
        likes = in.readInt();
        likeStatus = in.readByte() != 0;
        favoriteStatus = in.readByte() != 0;
        PersonalHospital = in.readString();
        insurance = in.readString();
        testname = in.readString();
        testfee = in.readString();
        availablepackege = in.readString();
        isCheck = in.readByte() != 0;
        isChecked = in.readByte() != 0;
        ischeck = in.readByte() != 0;
        address1 = in.readString();
        latitute1 = in.readString();
        longitute1 = in.readString();
        whrgivendiscount = in.readString();
        whrdiscountedfee = in.readString();
        pearson_clinical_name = in.readString();
    }

    public static final Creator<DoctorDetailsPojo> CREATOR = new Creator<DoctorDetailsPojo>() {
        @Override
        public DoctorDetailsPojo createFromParcel(Parcel in) {
            return new DoctorDetailsPojo(in);
        }

        @Override
        public DoctorDetailsPojo[] newArray(int size) {
            return new DoctorDetailsPojo[size];
        }
    };

    public String getWhrdiscountedfee() {
        return whrdiscountedfee;
    }

    public void setWhrdiscountedfee(String whrdiscountedfee) {
        this.whrdiscountedfee = whrdiscountedfee;
    }

    public String getWhrgivendiscount() {
        return whrgivendiscount;
    }

    public void setWhrgivendiscount(String whrgivendiscount) {
        this.whrgivendiscount = whrgivendiscount;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getLatitute1() {
        return latitute1;
    }

    public void setLatitute1(String latitute1) {
        this.latitute1 = latitute1;
    }

    public String getLongitute1() {
        return longitute1;
    }

    public void setLongitute1(String longitute1) {
        this.longitute1 = longitute1;
    }

    public int getQuata() {
        return quata;
    }

    public void setQuata(int quata) {
        this.quata = quata;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getPearson_clinical_name() {
        return pearson_clinical_name;
    }

    public void setPearson_clinical_name(String pearson_clinical_name) {
        this.pearson_clinical_name = pearson_clinical_name;
    }

    private String pearson_clinical_name;


    private List<Clinicname> Clinicname;


    public int getSpecialid() {
        return specialid;
    }

    public void setSpecialid(int specialid) {
        this.specialid = specialid;
    }

    public int getTotalDist() {
        return totalDist;
    }

    public void setTotalDist(int totalDist) {
        this.totalDist = totalDist;
    }


    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getPathy() {
        return pathy;
    }

    public void setPathy(String pathy) {
        this.pathy = pathy;
    }


    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDelivery() {
        return delivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getquata() {
        return quata;
    }

    public void setquata(int quata) {
        this.quata = quata;
    }


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public ArrayList<Clinicimages> getClinicimages() {
        return clinicimages;
    }

    public void setClinicimages(ArrayList<Clinicimages> clinicimages) {
        this.clinicimages = clinicimages;
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

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }


    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    public Double getLatitute() {
        return latitute;
    }

    public void setLatitute(Double latitute) {
        this.latitute = latitute;
    }

    public Double getLongitute() {
        return longitute;
    }

    public void setLongitute(Double longitute) {
        this.longitute = longitute;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonalHospital() {
        return PersonalHospital;
    }

    public void setPersonalHospital(String personalHospital) {
        PersonalHospital = personalHospital;
    }



/*    private String personalHospital;
    int personalHospitalId;*/


    public List<DoctorDetailsPojo.Clinicname> getClinicname() {
        return Clinicname;
    }

    public void setClinicname(List<DoctorDetailsPojo.Clinicname> clinicname) {
        Clinicname = clinicname;
    }


    public boolean isFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(sid);
        parcel.writeString(name);
        parcel.writeString(addr);
        parcel.writeString(cno);
        parcel.writeString(special);
        parcel.writeInt(specialid);
        parcel.writeString(images);
        parcel.writeInt(totalDist);
        parcel.writeString(profile_img);
        parcel.writeString(pathy);
        if (latitute == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitute);
        }
        if (longitute == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitute);
        }
        parcel.writeString(address);
        parcel.writeString(education);
        parcel.writeString(experience);
        parcel.writeString(delivery);
        parcel.writeInt(reviews);
        parcel.writeInt(quata);
        parcel.writeString(fee);
        if (distance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(distance);
        }
        parcel.writeInt(likes);
        parcel.writeByte((byte) (likeStatus ? 1 : 0));
        parcel.writeByte((byte) (favoriteStatus ? 1 : 0));
        parcel.writeString(PersonalHospital);
        parcel.writeString(insurance);
        parcel.writeString(testname);
        parcel.writeString(testfee);
        parcel.writeString(availablepackege);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
        parcel.writeByte((byte) (isChecked ? 1 : 0));
        parcel.writeByte((byte) (ischeck ? 1 : 0));
        parcel.writeString(address1);
        parcel.writeString(latitute1);
        parcel.writeString(longitute1);
        parcel.writeString(whrgivendiscount);
        parcel.writeString(whrdiscountedfee);
        parcel.writeString(pearson_clinical_name);
    }


    public static class Clinicname {
        private String clinicname;
        private int hid;

        public int getHid() {
            return hid;
        }

        public void setHid(int hid) {
            this.hid = hid;
        }


        public String getClinicname() {
            return clinicname;
        }

        public void setClinicname(String clinicname) {
            this.clinicname = clinicname;
        }
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

        @Override
        public String toString() {
            return "Clinicimages{" +
                    "imagespath='" + imagespath + '\'' +
                    '}';
        }

        public Clinicimages() {
        }

        protected Clinicimages(Parcel in) {
            imagespath = in.readString();
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
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(imagespath);
        }
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTestfee() {
        return testfee;
    }

    public void setTestfee(String testfee) {
        this.testfee = testfee;
    }

    public String getAvailablepackege() {
        return availablepackege;
    }

    public void setAvailablepackege(String availablepackege) {
        this.availablepackege = availablepackege;
    }

    //end of class


    @Override
    public String toString() {
        return "DoctorDetailsPojo{" +
                "id=" + id +
                ", sid=" + sid +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", cno='" + cno + '\'' +
                ", special='" + special + '\'' +
                ", specialid=" + specialid +
                ", images='" + images + '\'' +
                ", totalDist=" + totalDist +
                ", profile_img='" + profile_img + '\'' +
                ", pathy='" + pathy + '\'' +
                ", latitute=" + latitute +
                ", longitute=" + longitute +
                ", address='" + address + '\'' +
                ", clinicimages=" + clinicimages +
                ", education='" + education + '\'' +
                ", experience='" + experience + '\'' +
                ", delivery='" + delivery + '\'' +
                ", reviews=" + reviews +
                ", quata=" + quata +
                ", fee='" + fee + '\'' +
                ", distance=" + distance +
                ", likes=" + likes +
                ", likeStatus=" + likeStatus +
                ", favoriteStatus=" + favoriteStatus +
                ", PersonalHospital='" + PersonalHospital + '\'' +
                ", insurance='" + insurance + '\'' +
                ", testname='" + testname + '\'' +
                ", testfee='" + testfee + '\'' +
                ", availablepackege='" + availablepackege + '\'' +
                ", isCheck=" + isCheck +
                ", isChecked=" + isChecked +
                ", ischeck=" + ischeck +
                ", address1='" + address1 + '\'' +
                ", latitute1='" + latitute1 + '\'' +
                ", longitute1='" + longitute1 + '\'' +
                ", whrgivendiscount='" + whrgivendiscount + '\'' +
                ", whrdiscountedfee='" + whrdiscountedfee + '\'' +
                ", pearson_clinical_name='" + pearson_clinical_name + '\'' +
                ", Clinicname=" + Clinicname +
                '}';
    }
}