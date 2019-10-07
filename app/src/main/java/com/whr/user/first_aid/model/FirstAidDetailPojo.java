package com.whr.user.first_aid.model;

public class FirstAidDetailPojo {
    String username;
    String userreview;
    String reviewdate;

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserreview() {
        return userreview;
    }

    public void setUserreview(String userreview) {
        this.userreview = userreview;
    }

    @Override
    public String toString() {
        return "FirstAidDetailPojo{" +
                "username='" + username + '\'' +
                ", userreview='" + userreview + '\'' +
                '}';
    }
}
