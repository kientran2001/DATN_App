package com.example.datnapp.model;

import java.util.Date;

public class Record {
    private String waterMeterId;
    private Double value;
    private Date date;
    private String recorderName;
    private String recorderPhone;
    private String image;

    public Record(String waterMeterId, Double value, Date date, String recorderName, String recorderPhone, String image) {
        this.waterMeterId = waterMeterId;
        this.value = value;
        this.date = date;
        this.recorderName = recorderName;
        this.recorderPhone = recorderPhone;
        this.image = image;
    }

    public String getWaterMeterId() {
        return waterMeterId;
    }

    public void setWaterMeterId(String waterMeterId) {
        this.waterMeterId = waterMeterId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public String getRecorderPhone() {
        return recorderPhone;
    }

    public void setRecorderPhone(String recorderPhone) {
        this.recorderPhone = recorderPhone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Record{" +
                "waterMeterId='" + waterMeterId + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", recorderName='" + recorderName + '\'' +
                ", recorderPhone='" + recorderPhone + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
