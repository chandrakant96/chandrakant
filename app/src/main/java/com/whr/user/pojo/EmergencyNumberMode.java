package com.whr.user.pojo;

/**
 * Created by lenovo on 1/7/2017.
 */

public class EmergencyNumberMode {
    public String getEmgContactimage() {
        return emgContactimage;
    }

    public void setEmgContactimage(String emgContactimage) {
        this.emgContactimage = emgContactimage;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmgNumber() {
        return emgNumber;
    }

    public void setEmgNumber(String emgNumber) {
        this.emgNumber = emgNumber;
    }

    String emgContactimage, emergencyContactName;
    String emgNumber;

    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    String relationShip;

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    long eid;

    @Override
    public String toString() {
        return "EmergencyNumberMode{" +
                "emgContactimage='" + emgContactimage + '\'' +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", emgNumber='" + emgNumber + '\'' +
                ", relationShip='" + relationShip + '\'' +
                ", eid=" + eid +
                '}';
    }
}
