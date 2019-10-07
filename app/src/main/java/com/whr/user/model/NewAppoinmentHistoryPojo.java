package com.whr.user.model;

public class NewAppoinmentHistoryPojo {
    String BookingId;
    String BookedAt;
    String AppointmentDate;
    String AppointmentTime;
    String TestOrTreatmentDetails;
    String Specialization;
    String profile;
    String CancelFlag;
    String id;
    String hid;
    boolean isdatePassed = false;

    @Override
    public String toString() {
        return "NewAppoinmentHistoryPojo{" +
                "BookingId='" + BookingId + '\'' +
                ", BookedAt='" + BookedAt + '\'' +
                ", AppointmentDate='" + AppointmentDate + '\'' +
                ", AppointmentTime='" + AppointmentTime + '\'' +

                ", isdatePassed=" + isdatePassed +
                '}';
    }

    public boolean isIsdatePassed() {
        return isdatePassed;
    }

    public void setIsdatePassed(boolean isdatePassed) {
        this.isdatePassed = isdatePassed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }


    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getBookedAt() {
        return BookedAt;
    }

    public void setBookedAt(String bookedAt) {
        BookedAt = bookedAt;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getTestOrTreatmentDetails() {
        return TestOrTreatmentDetails;
    }

    public void setTestOrTreatmentDetails(String testOrTreatmentDetails) {
        TestOrTreatmentDetails = testOrTreatmentDetails;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCancelFlag() {
        return CancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        CancelFlag = cancelFlag;
    }


}
