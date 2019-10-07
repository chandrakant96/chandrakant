package com.whr.user.model;


import com.google.gson.annotations.SerializedName;

public class PromocodeResponce {

    private  String userid;

    private  String promocode;

    @SerializedName("total_pay")
    private  double totalPay;

    private  boolean status;

    @SerializedName("calculated_total")
    private  double calculatedTotal;

    @SerializedName("calculated_total_pay")
    private  double calculatedTotalPay;

    private  String message;

    @Override
    public String toString() {
        return "PromocodeResponce{" +
                "userid='" + userid + '\'' +
                ", promocode='" + promocode + '\'' +
                ", totalPay=" + totalPay +
                ", status=" + status +
                ", calculatedTotal=" + calculatedTotal +
                ", calculatedTotalPay=" + calculatedTotalPay +
                ", message='" + message + '\'' +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getCalculatedTotal() {
        return calculatedTotal;
    }

    public void setCalculatedTotal(double calculatedTotal) {
        this.calculatedTotal = calculatedTotal;
    }

    public double getCalculatedTotalPay() {
        return calculatedTotalPay;
    }

    public void setCalculatedTotalPay(double calculatedTotalPay) {
        this.calculatedTotalPay = calculatedTotalPay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
