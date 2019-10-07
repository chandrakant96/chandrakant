package com.whr.user.booking.packages.hospital.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whr.user.booking.doctor.booking.models.HospitalProfile;

import java.util.List;


public class PackageInfo implements Parcelable {
    private int id;

    private String packegename;

    private String description;

    private List<HospitalProfile.HospitalPackages.Packagesrelation> packagesrelation;

    protected PackageInfo(Parcel in) {
        id = in.readInt();
        packegename = in.readString();
        description = in.readString();
        packagesrelation = in.createTypedArrayList(HospitalProfile.HospitalPackages.Packagesrelation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(packegename);
        dest.writeString(description);
        dest.writeTypedList(packagesrelation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageInfo> CREATOR = new Creator<PackageInfo>() {
        @Override
        public PackageInfo createFromParcel(Parcel in) {
            return new PackageInfo(in);
        }

        @Override
        public PackageInfo[] newArray(int size) {
            return new PackageInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackegename() {
        return packegename;
    }

    public void setPackegename(String packegename) {
        this.packegename = packegename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<HospitalProfile.HospitalPackages.Packagesrelation> getPackagesrelation() {
        return packagesrelation;
    }

    public void setPackagesrelation(List<HospitalProfile.HospitalPackages.Packagesrelation> packagesrelation) {
        this.packagesrelation = packagesrelation;
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "id=" + id +
                ", packegename='" + packegename + '\'' +
                ", description='" + description + '\'' +
                ", packagesrelation=" + packagesrelation +
                '}';
    }
}
