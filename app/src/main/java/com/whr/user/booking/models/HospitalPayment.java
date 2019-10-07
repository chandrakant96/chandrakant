package com.whr.user.booking.models;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class HospitalPayment {
    @SerializedName("PayOnlineAmt")
    private double payOnlineAmt;

    @SerializedName("PayAtHospitalAmt")
    private double payAtHospitalAmt;

    @SerializedName("content")
    private String content;

    @SerializedName("TermsAndCondition")
    private String termsAndCondition;

    public HospitalPayment(int payOnlineAmt, int payAtHospitalAmt, String content,
                           String termsAndCondition) {
        this.payOnlineAmt = payOnlineAmt;
        this.payAtHospitalAmt = payAtHospitalAmt;
        this.content = content;
        this.termsAndCondition = termsAndCondition;
    }

    public double getPayOnlineAmt() {
        DecimalFormat precision = new DecimalFormat("0.00");
        payOnlineAmt = Double.parseDouble(precision.format(payOnlineAmt));
        return payOnlineAmt;
    }

    public double getPayAtHospitalAmt() {
        return payAtHospitalAmt;
    }

    public String getContent() {
        return content;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
    }
}
