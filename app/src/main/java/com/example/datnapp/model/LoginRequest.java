package com.example.datnapp.model;

import java.math.BigInteger;

public class LoginRequest {
    private BigInteger phoneNumber;
    private String password;

    public LoginRequest(BigInteger phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public BigInteger getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(BigInteger phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "phoneNumber=" + phoneNumber +
                ", password='" + password + '\'' +
                '}';
    }
}
