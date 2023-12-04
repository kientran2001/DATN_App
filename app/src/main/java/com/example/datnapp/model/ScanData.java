package com.example.datnapp.model;


public class ScanData {
    private String waterMeterId;
    private String name;
    private String phoneNumber;
    private String building;
    private String homeCode;
    private String address;

    public ScanData(String waterMeterId, String name, String phoneNumber, String building, String homeCode, String address) {
        this.waterMeterId = waterMeterId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.building = building;
        this.homeCode = homeCode;
        this.address = address;
    }

    public String getWaterMeterId() {
        return waterMeterId;
    }

    public void setWaterMeterId(String waterMeterId) {
        this.waterMeterId = waterMeterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getHomeCode() {
        return homeCode;
    }

    public void setHomeCode(String homeCode) {
        this.homeCode = homeCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
