package com.whr.user.pojo;

import com.whr.user.pojo.DoctorPojo.DoctorDetailsPojo;

import java.util.List;

/**
 * Created by lenovo on 1/30/2017.
 */

public class DoctorPathalogyListPojo {

    String docPathName;
    String docPathAddress;

    private List<DoctorDetailsPojo.Clinicimages> clinicimages;

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    String special;

    public String getDocPathProfileImage() {
        return docPathProfileImage;
    }

    public void setDocPathProfileImage(String docPathProfileImage) {
        this.docPathProfileImage = docPathProfileImage;
    }

    String docPathProfileImage;


    int docPathId;

    public List<DoctorDetailsPojo.Clinicimages> getClinicimages() {
        return clinicimages;
    }

    public void setClinicimages(List<DoctorDetailsPojo.Clinicimages> clinicimages) {
        this.clinicimages = clinicimages;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEduction() {
        return eduction;
    }

    public void setEduction(String eduction) {
        this.eduction = eduction;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    String cname, eduction, experience, cno, profile_img;


    public String getDocPathName() {
        return docPathName;
    }

    public void setDocPathName(String docPathName) {
        this.docPathName = docPathName;
    }

    public String getDocPathAddress() {
        return docPathAddress;
    }

    public void setDocPathAddress(String docPathAddress) {
        this.docPathAddress = docPathAddress;
    }

    public int getDocPathId() {
        return docPathId;
    }

    public void setDocPathId(int docPathId) {
        this.docPathId = docPathId;
    }
}
