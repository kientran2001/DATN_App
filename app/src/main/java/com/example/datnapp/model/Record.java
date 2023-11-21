package com.example.datnapp.model;

import java.math.BigInteger;
import java.util.Date;

public class Record {
    private String waterMeterId;
    private Double value;
    private Date date;
    private BigInteger recorder;

    public Record(String waterMeterId, Double value, Date date, BigInteger recorder) {
        this.waterMeterId = waterMeterId;
        this.value = value;
        this.date = date;
        this.recorder = recorder;
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

    @Override
    public String toString() {
        return "Record{" +
                "waterMeterId='" + waterMeterId + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", recorder=" + recorder +
                '}';
    }
}
