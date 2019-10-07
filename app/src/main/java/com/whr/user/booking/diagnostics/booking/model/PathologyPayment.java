package com.whr.user.booking.diagnostics.booking.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class PathologyPayment  {
    @SerializedName("OnlinePayment")
    private OnlinePayment onlinePayment;

    @SerializedName("PayAtPathology")
    private PayAtPathology PayAtPathology;

    public PathologyPayment(OnlinePayment onlinePayment, PayAtPathology PayAtPathology) {
        this.onlinePayment = onlinePayment;
        this.PayAtPathology = PayAtPathology;
    }

    public OnlinePayment getOnlinePayment() {
        return onlinePayment;
    }

    public PayAtPathology getPayAtPathology() {
        return PayAtPathology;
    }

    public static class OnlinePayment {

        @Override
        public String toString() {
            return "OnlinePayment{" +
                    "totalAmount=" + totalAmount +
                    ", walletTotal=" + walletTotal +
                    ", content='" + content + '\'' +
                    ", termsAndCondition='" + termsAndCondition + '\'' +
                    '}';
        }

        @SerializedName("TotalAmount")
        private double totalAmount;

        @SerializedName("Wallet_Total")
        private double walletTotal;

        private String content;

        @SerializedName("TermsAndCondition")
        private String termsAndCondition;

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

    public static class PayAtPathology {

        @Override
        public String toString() {
            return "PayAtPathology{" +
                    "payOnlineAmt=" + payOnlineAmt +
                    ", PayAtPathlogyAmt=" + PayAtPathlogyAmt +
                    ", content='" + content + '\'' +
                    ", termsAndCondition='" + termsAndCondition + '\'' +
                    '}';
        }

        @SerializedName("PayOnlineAmt")
        private double payOnlineAmt;

        @SerializedName("PayAtPathlogyAmt")
        private double PayAtPathlogyAmt;

        private String content;

        @SerializedName("TermsAndCondition")
        private String termsAndCondition;

        public PayAtPathology(int payOnlineAmt, int PayAtPathlogyAmt, String content, String termsAndCondition) {
            this.payOnlineAmt = payOnlineAmt;
            this.PayAtPathlogyAmt = PayAtPathlogyAmt;
            this.content = content;
            this.termsAndCondition = termsAndCondition;
        }

        public double getPayOnlineAmt() {
            DecimalFormat precision = new DecimalFormat("0.00");
            payOnlineAmt = Double.parseDouble(precision.format(payOnlineAmt));
            return payOnlineAmt;
        }

        public double getPayAtPathlogyAmt() {
            DecimalFormat precision = new DecimalFormat("##.##");
            PayAtPathlogyAmt = Double.parseDouble(precision.format(PayAtPathlogyAmt));
            return PayAtPathlogyAmt;
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
                ", PayAtPathology=" + PayAtPathology +
                '}';
    }
}
