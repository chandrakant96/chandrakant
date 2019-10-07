package com.whr.user.booking.models;


import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class DoctorPayment {
    @SerializedName("OnlinePayment")
    private OnlinePayment onlinePayment;

    @SerializedName("PayAtHospital")
    private PayAtHospital payAtHospital;

    public DoctorPayment(OnlinePayment onlinePayment, PayAtHospital payAtHospital) {
        this.onlinePayment = onlinePayment;
        this.payAtHospital = payAtHospital;
    }

    public OnlinePayment getOnlinePayment() {
        return onlinePayment;
    }

    public PayAtHospital getPayAtHospital() {
        return payAtHospital;
    }

    public static class OnlinePayment {

        @SerializedName("TotalAmount")
        private double totalAmount;

        @SerializedName("Wallet_Total")
        private double walletTotal;

        private double wallet_amount = 0;

        private String content;

        @SerializedName("TermsAndCondition")
        private String termsAndCondition;

        public double getWallet_amount() {
            walletTotal = totalAmount - walletTotal;
            DecimalFormat precision = new DecimalFormat("0.00");
            walletTotal = Double.parseDouble(precision.format(walletTotal));
            return walletTotal;
        }

        public OnlinePayment(int totalAmount, double walletTotal, String content,
                             String termsAndCondition) {
            this.totalAmount = totalAmount;
            this.walletTotal = walletTotal;
            this.content = content;
            this.termsAndCondition = termsAndCondition;
        }

        public double getTotalAmount() {
            DecimalFormat precision = new DecimalFormat("0.00");
            totalAmount = Double.parseDouble(precision.format(totalAmount));
            return totalAmount;
        }

        public double getWalletTotal() {
            DecimalFormat precision = new DecimalFormat("0.00");
            walletTotal = Double.parseDouble(precision.format(walletTotal));
            return walletTotal;
        }

        public String getContent() {
            return content;
        }

        public String getTermsAndCondition() {
            return termsAndCondition;
        }
    }

    public static class PayAtHospital {

        @Override
        public String toString() {
            return "PayAtHospital{" +
                    "payOnlineAmt=" + payOnlineAmt +
                    ", payAtHospitalAmt=" + payAtHospitalAmt +
                    ", content='" + content + '\'' +
                    ", termsAndCondition='" + termsAndCondition + '\'' +
                    '}';
        }

        @SerializedName("PayOnlineAmt")
        private double payOnlineAmt;

        @SerializedName("PayAtHospitalAmt")
        private double payAtHospitalAmt;

        private String content;

        @SerializedName("TermsAndCondition")
        private String termsAndCondition;

        public PayAtHospital(int payOnlineAmt, int payAtHospitalAmt, String content,
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
            DecimalFormat precision = new DecimalFormat("0.00");
            payAtHospitalAmt = Double.parseDouble(precision.format(payAtHospitalAmt));
            return payAtHospitalAmt;
        }

        public String getContent() {
            return content;
        }

        public String getTermsAndCondition() {
            return termsAndCondition;
        }
    }

    @Override
    public String toString() {
        return "DoctorPayment{" +
                "onlinePayment=" + onlinePayment +
                ", payAtHospital=" + payAtHospital +
                '}';
    }
}
