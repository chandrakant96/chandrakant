package com.whr.user.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by whrhealth on 3/29/18.
 */

public class CentralSearchModel implements Parcelable {

    @SerializedName("Id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("Type")
    private String type;
    @SerializedName("TypeId")
    private String typeId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(typeId);
    }

    public CentralSearchModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        typeId = in.readString();
    }

    public CentralSearchModel() {

    }

    public static final Creator<CentralSearchModel> CREATOR = new Creator<CentralSearchModel>() {
        @Override
        public CentralSearchModel createFromParcel(Parcel in) {
            return new CentralSearchModel(in);
        }

        @Override
        public CentralSearchModel[] newArray(int size) {
            return new CentralSearchModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public static Creator<CentralSearchModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "CentralSearchModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", typeId='" + typeId + '\'' +
                '}';
    }
}
