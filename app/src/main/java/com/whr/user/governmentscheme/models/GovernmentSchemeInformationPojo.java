package com.whr.user.governmentscheme.models;

public class GovernmentSchemeInformationPojo {
    String id;
    String name;
    String ContactNumber;
    String Address;

    @Override
    public String toString() {
        return "GovernmentSchemeInformationPojo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ContactNumber='" + ContactNumber + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }

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

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
