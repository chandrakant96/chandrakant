package com.whr.user.booking.diagnostics.booking.model;

import java.util.List;

public class PathologyDetailList {

    private List<Reviews> reviews;
    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }
    public static class Pathologyprofile
    {
        String id;
        String PathologyName;
        String address;
        String likes;
        String reviews;
        String profile_img;
        String type;
        double latitude;
        double longitude;
        double distance;
        String workingfrom;
        String workingto;
        boolean likeStatus;
        boolean favoriteStatus;



        @Override
        public String toString() {
            return "PathologyListPojo{" +
                    "id='" + id + '\'' +
                    ", PathologyName='" + PathologyName + '\'' +
                    ", address='" + address + '\'' +
                    ", likes='" + likes + '\'' +
                    ", reviews='" + reviews + '\'' +
                    ", profile_img='" + profile_img + '\'' +
                    ", type='" + type + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", distance=" + distance +
                    ", workingfrom='" + workingfrom + '\'' +
                    ", workingto='" + workingto + '\'' +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPathologyName() {
            return PathologyName;
        }

        public void setPathologyName(String pathologyName) {
            PathologyName = pathologyName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getReviews() {
            return reviews;
        }

        public void setReviews(String reviews) {
            this.reviews = reviews;
        }

        public String getProfile_img() {
            return profile_img;
        }

        public void setProfile_img(String profile_img) {
            this.profile_img = profile_img;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getWorkingfrom() {
            return workingfrom;
        }

        public void setWorkingfrom(String workingfrom) {
            this.workingfrom = workingfrom;
        }

        public String getWorkingto() {
            return workingto;
        }

        public void setWorkingto(String workingto) {
            this.workingto = workingto;
        }

        public boolean isLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(boolean likeStatus) {
            this.likeStatus = likeStatus;
        }

        public boolean isFavoriteStatus() {
            return favoriteStatus;
        }

        public void setFavoriteStatus(boolean favoriteStatus) {
            this.favoriteStatus = favoriteStatus;
        }

    }
    public static class Reviews {
        @Override
        public String toString() {
            return "Reviews{" +
                    "username='" + username + '\'' +
                    ", userreview='" + userreview + '\'' +
                    '}';
        }

        private String username;

        private String userreview;

        public Reviews(String username, String userreview) {
            this.username = username;
            this.userreview = userreview;
        }

        public String getUsername() {
            return username;
        }

        public String getUserreview() {
            return userreview;
        }
    }
}
