package com.whr.user.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PaymentReceiptModel {
    @SerializedName("PatientName")
    private String patientName;

    @SerializedName("PatientMobileNo")
    private String patientMobileNo;

    @SerializedName("PatientAge")
    private String patientAge;

    @SerializedName("DateOfBooking")
    private String dateOfBooking;

    @SerializedName("BookingId")
    private String bookingId;

    @SerializedName("Specialization")
    private String specialization;

    @SerializedName("BookedBy")
    private String bookedBy;

    @SerializedName("BookedAt")
    private String bookedAt;

    @SerializedName("Address")
    private String address;

    @SerializedName("AppointmentDate")
    private String appointmentDate;

    @SerializedName("AppointmentTime")
    private String appointmentTime;

    @SerializedName("Total")
    private String total;

    @SerializedName("WalletDiscount")
    private String walletDiscount;

    @SerializedName("OfferDiscount")
    private String offerDiscount;

    @SerializedName("PayAtHospital")
    private String payAtHospital;

    @SerializedName("OnlinePaid")
    private String onlinePaid;

    @SerializedName("TestOrTreatmentDetails")
    private List<TestOrTreatmentDetails> testOrTreatmentDetails;

    @SerializedName("cancelationpolicy")
    private String cancelationpolicy;

    @SerializedName("bookingpolicy")
    private String bookingpolicy;

    @SerializedName("PromoCode")
    private String promocode;




    public PaymentReceiptModel(String patientName, String patientMobileNo, String patientAge,
                               String dateOfBooking, String bookingId, String specialization, String bookedBy,
                               String bookedAt, String address, String appointmentDate, String appointmentTime,
                               String total, String walletDiscount, String offerDiscount, String payAtHospital,
                               String onlinePaid, List<TestOrTreatmentDetails> testOrTreatmentDetails,
                               String cancelationpolicy, String bookingpolicy) {
        this.patientName = patientName;
        this.patientMobileNo = patientMobileNo;
        this.patientAge = patientAge;
        this.dateOfBooking = dateOfBooking;
        this.bookingId = bookingId;
        this.specialization = specialization;
        this.bookedBy = bookedBy;
        this.bookedAt = bookedAt;
        this.address = address;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.total = total;
        this.walletDiscount = walletDiscount;
        this.offerDiscount = offerDiscount;
        this.payAtHospital = payAtHospital;
        this.onlinePaid = onlinePaid;
        this.testOrTreatmentDetails = testOrTreatmentDetails;
        this.cancelationpolicy = cancelationpolicy;
        this.bookingpolicy = bookingpolicy;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientMobileNo() {
        return patientMobileNo;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getDateOfBooking() {
        return dateOfBooking;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public String getAddress() {
        return address;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getTotal() {
        return total;
    }

    public String getWalletDiscount() {
        return walletDiscount;
    }

    public String getOfferDiscount() {
        return offerDiscount;
    }

    public String getPayAtHospital() {
        return payAtHospital;
    }

    public String getOnlinePaid() {
        return onlinePaid;
    }

    public List<TestOrTreatmentDetails> getTestOrTreatmentDetails() {
        return testOrTreatmentDetails;
    }

    public String getCancelationpolicy() {
        return cancelationpolicy;
    }

    public String getBookingpolicy() {
        return bookingpolicy;
    }

    public static class TestOrTreatmentDetails {
        @SerializedName("PaymentTableId")
        private Object paymentTableId;

        @SerializedName("TestOrTreatmentOrPackageId")
        private Object testOrTreatmentOrPackageId;

        @SerializedName("TestOrTreatmentOrPackageName")
        private String testOrTreatmentOrPackageName;

        @SerializedName("DicountPrice")
        private String dicountPrice;

        @SerializedName("OriginalPrice")
        private String originalPrice;

        public TestOrTreatmentDetails(Object paymentTableId, Object testOrTreatmentOrPackageId,
                                      String testOrTreatmentOrPackageName, String dicountPrice, String originalPrice) {
            this.paymentTableId = paymentTableId;
            this.testOrTreatmentOrPackageId = testOrTreatmentOrPackageId;
            this.testOrTreatmentOrPackageName = testOrTreatmentOrPackageName;
            this.dicountPrice = dicountPrice;
            this.originalPrice = originalPrice;
        }


        public String getTestOrTreatmentOrPackageName() {
            return testOrTreatmentOrPackageName;
        }

        public String getDicountPrice() {
            return dicountPrice;
        }

    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPromocode() {
        return promocode;
    }
}