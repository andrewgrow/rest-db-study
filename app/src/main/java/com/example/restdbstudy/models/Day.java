package com.example.restdbstudy.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.restdbstudy.App;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Модель базы данных которая хранит расписание для конкретного дня
 */
@Entity
public class Day {

    @PrimaryKey(autoGenerate = true)
    public long id;

    private String nameOfDay; // день недели

    private String listScheduleAsJson; // расписание на этот день в виде JSON

    public String getNameOfDay() { // получение названия дня
        return nameOfDay;
    }

    public void setNameOfDay(String nameOfDay) { // установка названия для дня
        this.nameOfDay = nameOfDay;
    }

    /**
     * @return список расписания конкретного дня из сохранённого в виде json
     */
    public List<String> getListSchedule() {
        return App.getGson().fromJson(listScheduleAsJson, getTypeToken());
    }

    /**
     * Сохраняет расписание, преобразовывая его из списка в строку (json)
     * @param listSchedule
     */
    public void setListSchedule(List<String> listSchedule) {
        listScheduleAsJson = App.getGson().toJson(listSchedule, getTypeToken());
    }

    /**
     * @return токен типа для списка расписаний. Используется при конвертации JSON.
     */
    private Type getTypeToken() {
        return new TypeToken<List<String>>(){}.getType();
    }

    public void setListScheduleAsJson(String listScheduleAsJson) {
        // база будет использовать этот метод для сохранения строки в памяти
        this.listScheduleAsJson = listScheduleAsJson;
    }

    public String getListScheduleAsJson() {
        // база будет использовать этот метод для получения строки из памяти
        return listScheduleAsJson;
    }
}
