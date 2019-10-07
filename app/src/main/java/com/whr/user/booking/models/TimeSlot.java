package com.whr.user.booking.models;

import java.util.Objects;

public class TimeSlot {

    private String strtime;

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStrtime() {
        return strtime;
    }

    public void setStrtime(String strtime) {
        this.strtime = strtime;
    }

    private boolean isPassed = false;

    private boolean isBooked = false;

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "strtime='" + strtime + '\'' +
                ", isChecked=" + isChecked +
                ", isPassed=" + isPassed +
                ", isBooked=" + isBooked +
                '}';
    }
}
