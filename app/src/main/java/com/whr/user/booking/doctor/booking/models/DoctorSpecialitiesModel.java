package com.whr.user.booking.doctor.booking.models;

import android.os.Parcel;
import android.os.Parcelable;


public class DoctorSpecialitiesModel implements Parcelable {
    int id;
    String name;
    String watermark;



    protected DoctorSpecialitiesModel(Parcel in) {
        id = in.readInt();
        name = in.readString();

        watermark = in.readString();

    }

    public static final Creator<DoctorSpecialitiesModel> CREATOR = new Creator<DoctorSpecialitiesModel>() {
        @Override
        public DoctorSpecialitiesModel createFromParcel(Parcel in) {
            return new DoctorSpecialitiesModel(in);
        }

        @Override
        public DoctorSpecialitiesModel[] newArray(int size) {
            return new DoctorSpecialitiesModel[size];
        }
    };

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public DoctorSpecialitiesModel() {
    }

    public DoctorSpecialitiesModel(String name, int i) {
        this.id = i;
        this.name = name;
    }


    @Override
    public String toString() {
        return "DoctorSpecialitiesModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", watermark='" + watermark + '\'' +

                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(watermark);
    }
}




































/*
package com.etech.medicaluser.pojo;

import android.content.Intent;

*/
/**
 * Created by Prashant on 03/10/2016.
 *//*


public class DoctorSpecialitiesModel
{


*/
/*

    Integer image;


    String Main_Specification;
    String Sub_MainSpecification;

*//*


    int id;
    String name;
    String other;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DoctorSpecialitiesModel(Integer image, String main_Specification, String sub_MainSpecification, int id)
    {
        this.image = image;
       this. Main_Specification = main_Specification;
        this.Sub_MainSpecification = sub_MainSpecification;

        this .id = id;
    }


    public DoctorSpecialitiesModel()
    {
    }





    public String getMain_Specification()
    {
        return Main_Specification;
    }

    public void setMain_Specification(String main_Specification) {
        Main_Specification = main_Specification;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getSub_MainSpecification() {
        return Sub_MainSpecification;
    }

    public void setSub_MainSpecification(String sub_MainSpecification) {
        Sub_MainSpecification = sub_MainSpecification;


}
}*/
