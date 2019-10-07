package com.whr.user.booking.diagnostics.booking.model;

public class ReceiptPojo {
    private String TestOrTreatmentOrPackageName;
    private String DicountPrice;

    public String getTestOrTreatmentOrPackageName() {
        return TestOrTreatmentOrPackageName;
    }

    public void setTestOrTreatmentOrPackageName(String testOrTreatmentOrPackageName) {
        TestOrTreatmentOrPackageName = testOrTreatmentOrPackageName;
    }

    public String getDicountPrice() {
        return DicountPrice;
    }

    public void setDicountPrice(String dicountPrice) {
        DicountPrice = dicountPrice;
    }
}
