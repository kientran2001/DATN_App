package com.example.datnapp.model;

import java.math.BigInteger;
import java.util.Date;

public class Record {
    private String waterMeterId;
    private Double value;
    private Date date;
    private String recorderName;
    private BigInteger recorderPhone;

    public Record(String waterMeterId, Double value, Date date, String recorderName, BigInteger recorderPhone) {
        this.waterMeterId = waterMeterId;
        this.value = value;
        this.date = date;
        this.recorderName = recorderName;
        this.recorderPhone = recorderPhone;
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

    public BigInteger getRecorderPhone() {
        return recorderPhone;
    }

    public void setRecorderPhone(BigInteger recorderPhone) {
        this.recorderPhone = recorderPhone;
    }

    @Override
    public String toString() {
        return "Record{" +
                "waterMeterId='" + waterMeterId + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", recorderName='" + recorderName + '\'' +
                ", recorderPhone=" + recorderPhone +
                '}';
    }
}
