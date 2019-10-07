package com.whr.user.pojo;

/**
 * Created by lenovo on 2/8/2017.
 */
public class DoctorListForPresPojo {

    int id;
    String doctorName, ContactNo, doctorAddress;
    public String Doc_profileImage;

    public int getPriscriptionId() {
        return priscriptionId;
    }

    public void setPriscriptionId(int priscriptionId) {
        this.priscriptionId = priscriptionId;
    }

    public String getPriscriptionImage() {
        return priscriptionImage;
    }

    public void setPriscriptionImage(String priscriptionImage) {
        this.priscriptionImage = priscriptionImage;
    }

    public String getPriscriptionDate() {
        return priscriptionDate;
    }

    public void setPriscriptionDate(String priscriptionDate) {
        this.priscriptionDate = priscriptionDate;
    }

    int priscriptionId;
    String priscriptionImage, priscriptionDate;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    int doctorId;


    public String getDoc_profileImage() {
        return Doc_profileImage;
    }

    public void setDoc_profileImage(String doc_profileImage) {
        Doc_profileImage = doc_profileImage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public DoctorListForPresPojo(String doctorName) {
        this.doctorName = doctorName;
    }
}
