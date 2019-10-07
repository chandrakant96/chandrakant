package com.whr.user.model;

public class NotificationActivityPojo {


    String title, time, message, image, type;

    public int getNoti_Id() {
        return noti_Id;
    }

    public void setNoti_Id(int noti_Id) {
        this.noti_Id = noti_Id;
    }

    public boolean isNoti_readFlag() {
        return noti_readFlag;
    }

    public void setNoti_readFlag(boolean noti_readFlag) {
        this.noti_readFlag = noti_readFlag;
    }

    boolean noti_readFlag;
    int noti_Id;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    int doctorId;

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    String userUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
