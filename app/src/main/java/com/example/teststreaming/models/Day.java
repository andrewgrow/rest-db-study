package com.example.teststreaming.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Day {

    @SerializedName("name")
    private String nameOfDay;

    @SerializedName("schedule")
    private List<String> listSchedule;

    public String getNameOfDay() {
        return nameOfDay;
    }

    public void setNameOfDay(String nameOfDay) {
        this.nameOfDay = nameOfDay;
    }

    public List<String> getListSchedule() {
        return listSchedule;
    }

    public void setListSchedule(List<String> listSchedule) {
        this.listSchedule = listSchedule;
    }
}
