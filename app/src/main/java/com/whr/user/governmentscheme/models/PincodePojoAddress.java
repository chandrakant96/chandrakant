package com.whr.user.governmentscheme.models;

public class PincodePojoAddress {

        String cityOrVillage;
        String Tehsil;
        String District;
        String state;
        String Pincode;

        @Override
        public String toString() {
            return "PincodePojo{" +
                    "cityOrVillage='" + cityOrVillage + '\'' +
                    ", Tehsil='" + Tehsil + '\'' +
                    ", District='" + District + '\'' +
                    ", state='" + state + '\'' +
                    ", Pincode='" + Pincode + '\'' +
                    '}';
        }

        public String getCityOrVillage() {
            return cityOrVillage;
        }

        public void setCityOrVillage(String cityOrVillage) {
            this.cityOrVillage = cityOrVillage;
        }

        public String getTehsil() {
            return Tehsil;
        }

        public void setTehsil(String tehsil) {
            Tehsil = tehsil;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String district) {
            District = district;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String pincode) {
            Pincode = pincode;
        }


}
