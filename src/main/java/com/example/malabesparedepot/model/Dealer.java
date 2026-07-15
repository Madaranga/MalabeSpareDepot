package com.example.malabesparedepot.model;

public class Dealer {
    private String dealerId;
    private String name;
    private String phoneNumber;
    private String location;

    public  Dealer(String dealerId, String name, String phoneNumber, String location) {
        this.dealerId = dealerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    // Getters
    public String getDealerId() {
        return dealerId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public String getLocation() {

        return location;
    }
}
