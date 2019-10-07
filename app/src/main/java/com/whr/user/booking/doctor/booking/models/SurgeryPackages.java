package com.whr.user.booking.doctor.booking.models;

import java.util.ArrayList;

public class SurgeryPackages {
    private String sliderImage;

    private String ImgDescription;

    public ArrayList<SurgeryPackages> surgeryPackages;

    public SurgeryPackages() {
    }

    public SurgeryPackages(String sliderImage, String ImgDescription, ArrayList<SurgeryPackages> surgeryPackages) {
        this.sliderImage = sliderImage;
        this.ImgDescription = ImgDescription;
        this.surgeryPackages = surgeryPackages;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getImgDescription() {
        return ImgDescription;
    }

    public void setImgDescription(String ImgDescription) {
        this.ImgDescription = ImgDescription;
    }

    public ArrayList<SurgeryPackages> getSurgeryPackages() {
        return surgeryPackages;
    }

    public void setSurgeryPackages(ArrayList<SurgeryPackages> surgeryPackages) {
        this.surgeryPackages = surgeryPackages;
    }
}
