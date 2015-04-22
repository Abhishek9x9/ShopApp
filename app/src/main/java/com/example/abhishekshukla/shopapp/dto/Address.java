package com.example.abhishekshukla.shopapp.dto;

/**
 * Created by abhishekshukla on 20/4/15.
 */
public class Address {

    private String name;
    private String pincode;
    private String address;
    private String town;
    private String city;
    private String state;
    private String mobileNumber;

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }

    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
