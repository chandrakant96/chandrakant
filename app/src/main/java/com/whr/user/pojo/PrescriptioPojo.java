package com.whr.user.pojo;

/**
 * Created by lenovo on 6/14/2017.
 */

public class PrescriptioPojo {

    String medicineName;
    int quantity;
    int medicineId;

    public String getMorniingQty() {
        return morniingQty;
    }

    public void setMorniingQty(String morniingQty) {
        this.morniingQty = morniingQty;
    }

    public String getAfternoonQty() {
        return afternoonQty;
    }

    public void setAfternoonQty(String afternoonQty) {
        this.afternoonQty = afternoonQty;
    }

    public String getNightQty() {
        return nightQty;
    }

    public void setNightQty(String nightQty) {
        this.nightQty = nightQty;
    }

    String morniingQty, afternoonQty, nightQty;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    double price;


    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }


    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
