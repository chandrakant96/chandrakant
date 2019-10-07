package com.whr.user.wallet;

import android.os.Parcel;
import android.os.Parcelable;

public class WallatPojo implements Parcelable {


    String totalPoints;
    String bookingId;
    String transactionDate;
    String testtreatmentpackagename;
    String crediteddebitedPoints;
    String type;

    protected WallatPojo(Parcel in) {
        totalPoints = in.readString();
        bookingId = in.readString();
        transactionDate = in.readString();
        testtreatmentpackagename = in.readString();
        crediteddebitedPoints = in.readString();
        type = in.readString();
        dochospathname = in.readString();
    }

    public static final Creator<WallatPojo> CREATOR = new Creator<WallatPojo>() {
        @Override
        public WallatPojo createFromParcel(Parcel in) {
            return new WallatPojo(in);
        }

        @Override
        public WallatPojo[] newArray(int size) {
            return new WallatPojo[size];
        }
    };

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTesttreatmentpackagename() {
        return testtreatmentpackagename;
    }

    public void setTesttreatmentpackagename(String testtreatmentpackagename) {
        this.testtreatmentpackagename = testtreatmentpackagename;
    }

    public String getCrediteddebitedPoints() {
        return crediteddebitedPoints;
    }

    public void setCrediteddebitedPoints(String crediteddebitedPoints) {
        this.crediteddebitedPoints = crediteddebitedPoints;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDochospathname() {
        return dochospathname;
    }

    public void setDochospathname(String dochospathname) {
        this.dochospathname = dochospathname;
    }

    String dochospathname;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(totalPoints);
        dest.writeString(bookingId);
        dest.writeString(transactionDate);
        dest.writeString(testtreatmentpackagename);
        dest.writeString(crediteddebitedPoints);
        dest.writeString(type);
        dest.writeString(dochospathname);
    }
}