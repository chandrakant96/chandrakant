package com.whr.user.pojo;

/**
 * Created by lenovo on 6/9/2017.
 */

public class PayBillPojo {


    String docName = "";
    String docAddress = "";
    String docspec = "";
    String docImag = "";
    String transactiontime;
    String totalAmount = "0.0";
    String uname = "";
    String transactiondate = "0.0";
    String doc_education = "";
    String did = "";
    String hospitalName = "";
    String experience = "", cname = "";
    String bookingpolicy = "", cancelationpolicy = "";
    String longitude = "0.0", latitude = "0.0";
    String phoneNo = "";
    String RescheduleDate = "";
    String olddate = "";


    public String getRescheduleDate() {
        return RescheduleDate;
    }

    public void setRescheduleDate(String rescheduleDate) {
        RescheduleDate = rescheduleDate;
    }

    public String getOlddate() {
        return olddate;
    }

    public void setOlddate(String olddate) {
        this.olddate = olddate;
    }

    String appointmenttime = "10:00 AM";

    public String getAppointmenttime() {
        return appointmenttime;
    }

    public void setAppointmenttime(String appointmenttime) {
        this.appointmenttime = appointmenttime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocAddress() {
        return docAddress;
    }

    public void setDocAddress(String docAddress) {
        this.docAddress = docAddress;
    }

    public String getDocspec() {
        return docspec;
    }

    public void setDocspec(String docspec) {
        this.docspec = docspec;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }


    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }


    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }


    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }


    public String getBookingpolicy() {
        return bookingpolicy;
    }

    public void setBookingpolicy(String bookingpolicy) {
        this.bookingpolicy = bookingpolicy;
    }

    public String getCancelationpolicy() {
        return cancelationpolicy;
    }

    public void setCancelationpolicy(String cancelationpolicy) {
        this.cancelationpolicy = cancelationpolicy;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getDoc_education() {
        return doc_education;
    }

    public void setDoc_education(String doc_education) {
        this.doc_education = doc_education;
    }


    public String getDocImag() {
        return docImag;
    }

    public void setDocImag(String docImag) {
        this.docImag = docImag;
    }


    public class TreatmentList {
        String tname = "", fee = "";
        String discount_price = "";
        String id = "";
        String tid = "";

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        String description;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getDiscount_price() {
            return discount_price;
        }

        public void setDiscount_price(String discount_price) {
            this.discount_price = discount_price;
        }


        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
    }

    @Override
    public String toString() {
        return "PayBillPojo{" +
                "docName='" + docName + '\'' +
                ", docAddress='" + docAddress + '\'' +
                ", docspec='" + docspec + '\'' +
                ", docImag='" + docImag + '\'' +
                ", transactiontime='" + transactiontime + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", uname='" + uname + '\'' +
                ", experience='" + experience + '\'' +
                ", cname='" + cname + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", transactiondate='" + transactiondate + '\'' +
                ", bookingpolicy='" + bookingpolicy + '\'' +
                ", cancelationpolicy='" + cancelationpolicy + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", doc_education='" + doc_education + '\'' +
                ", did=" + did +
                '}';
    }
}
