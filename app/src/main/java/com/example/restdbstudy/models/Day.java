package com.example.restdbstudy.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.restdbstudy.App;
import com.example.restdbstudy.database.Database;
import com.example.restdbstudy.database.IDatabaseAccessObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    /**
     * Сохранение списка дней в базу данных.
     * @param dayList
     */
    public static void saveDaysList(List<Day> dayList) {
        if (dayList == null || dayList.size() == 0) {
            return;
        }

        // обработка будет выполняться НЕ в мейн треде.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // получаем объект базы данных
                Database db = App.getDatabase();
                // запрашиваем у базы нужный ДАО
                IDatabaseAccessObject dao = db.getDAO();
                // перебираем все дни и вставляем каждый в базу
                for (Day day : dayList) {
                    dao.insert(day);
                }
            }
        };
        // запускаем тред
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setListScheduleAsJson(String listScheduleAsJson) {
        // база будет использовать этот метод для сохранения строки в памяти
        this.listScheduleAsJson = listScheduleAsJson;
    }

    public String getListScheduleAsJson() {
        // база будет использовать этот метод для получения строки из памяти
        return listScheduleAsJson;
    }

    /**
     * @return сохранённый в памяти список расписаний
     */
    @NonNull
    public static List<Day> getCashedList() {
        // получаем объект базы данных
        Database db = App.getDatabase();
        // запрашиваем у базы нужный ДАО
        IDatabaseAccessObject dao = db.getDAO();
        // запрашиваем у ДАО список дней
        List<Day> result = dao.getAll();

        // если вдруг ДАО вернул null, то вернём новый пустой созданный список
        // (чтобы не было крешей у адаптера)
        if (result == null) {
            result = new ArrayList<>();
        }
        // результат
        return result;
    }
}
