package com.whr.user.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 12/9/2016.
 */

public class DoctorTreatmentPojo implements Parcelable {
    String Description;
    double fee;
    String treatmentname;
    int tid;
    boolean isCheck = false;
    double discount_price;
    int discount;
    //boolean isChecked;
    private String id;
    private String hid;
    private String count;
    private String HospitalPackagetableSecondId;
    private String Ward;
    int rowid;
    double whrdiscount, doctordiscountedprize;
    double Minfee = 0.0;
    double Maxfee = 0.0;

    public DoctorTreatmentPojo() {

    }

    @Override
    public String toString() {
        return "DoctorTreatmentPojo{" +
                "Description='" + Description + '\'' +
                ", fee=" + fee +
                ", treatmentname='" + treatmentname + '\'' +
                ", tid=" + tid +
                ", isCheck=" + isCheck +
                ", discount_price=" + discount_price +
                ", discount=" + discount +
                ", id='" + id + '\'' +
                ", hid='" + hid + '\'' +
                ", count='" + count + '\'' +
                ", HospitalPackagetableSecondId='" + HospitalPackagetableSecondId + '\'' +
                ", Ward='" + Ward + '\'' +
                ", rowid=" + rowid +
                ", whrdiscount=" + whrdiscount +
                ", doctordiscountedprize=" + doctordiscountedprize +
                ", Minfee=" + Minfee +
                ", Maxfee=" + Maxfee +
                '}';
    }

    protected DoctorTreatmentPojo(Parcel in) {
        Description = in.readString();
        fee = in.readDouble();
        treatmentname = in.readString();
        tid = in.readInt();
        isCheck = in.readByte() != 0;
        discount_price = in.readDouble();
        discount = in.readInt();
        id = in.readString();
        hid = in.readString();
        count = in.readString();
        HospitalPackagetableSecondId = in.readString();
        Ward = in.readString();
        rowid = in.readInt();
        whrdiscount = in.readDouble();
        doctordiscountedprize = in.readDouble();
        Minfee = in.readDouble();
        Maxfee = in.readDouble();
    }

    public static final Creator<DoctorTreatmentPojo> CREATOR = new Creator<DoctorTreatmentPojo>() {
        @Override
        public DoctorTreatmentPojo createFromParcel(Parcel in) {
            return new DoctorTreatmentPojo(in);
        }

        @Override
        public DoctorTreatmentPojo[] newArray(int size) {
            return new DoctorTreatmentPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Description);
        parcel.writeDouble(fee);
        parcel.writeString(treatmentname);
        parcel.writeInt(tid);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
        parcel.writeDouble(discount_price);
        parcel.writeInt(discount);
        parcel.writeString(id);
        parcel.writeString(hid);
        parcel.writeString(count);
        parcel.writeString(HospitalPackagetableSecondId);
        parcel.writeString(Ward);
        parcel.writeInt(rowid);
        parcel.writeDouble(whrdiscount);
        parcel.writeDouble(doctordiscountedprize);
        parcel.writeDouble(Minfee);
        parcel.writeDouble(Maxfee);
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getTreatmentname() {
        return treatmentname;
    }

    public void setTreatmentname(String treatmentname) {
        this.treatmentname = treatmentname;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getHospitalPackagetableSecondId() {
        return HospitalPackagetableSecondId;
    }

    public void setHospitalPackagetableSecondId(String hospitalPackagetableSecondId) {
        HospitalPackagetableSecondId = hospitalPackagetableSecondId;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public double getWhrdiscount() {
        return whrdiscount;
    }

    public void setWhrdiscount(double whrdiscount) {
        this.whrdiscount = whrdiscount;
    }

    public double getDoctordiscountedprize() {
        return doctordiscountedprize;
    }

    public void setDoctordiscountedprize(double doctordiscountedprize) {
        this.doctordiscountedprize = doctordiscountedprize;
    }

    public double getMinfee() {
        return Minfee;
    }

    public void setMinfee(double minfee) {
        Minfee = minfee;
    }

    public double getMaxfee() {
        return Maxfee;
    }

    public void setMaxfee(double maxfee) {
        Maxfee = maxfee;
    }

    public static Creator<DoctorTreatmentPojo> getCREATOR() {
        return CREATOR;
    }

}