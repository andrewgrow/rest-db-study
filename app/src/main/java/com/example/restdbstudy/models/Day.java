package com.example.restdbstudy.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Модель которая хранит расписание для конкретного дня
 */
public class Day {

    private String nameOfDay; // день недели

    private List<String> listSchedule; // расписание на этот день

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
