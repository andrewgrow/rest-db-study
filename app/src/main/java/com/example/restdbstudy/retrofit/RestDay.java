package com.example.restdbstudy.retrofit;

import android.support.annotation.NonNull;

import com.example.restdbstudy.models.Day;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Сетевая модель которая хранит расписание для конкретного дня и была получена нами от сервера.
 */
public class RestDay {

    @SerializedName("name")
    private String nameOfDay; // день недели

    @SerializedName("schedule")
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

    /**
     * Метод конвертации из сетевых моделей в модель базы данных
     * @param restDayList на вход получаем список сетевых моделей (дней с раписаниями от сервера)
     * @return возвращаем список дней в виде моделей базы данных (таких же дней с расписаниями)
     */
    @NonNull
    public static List<Day> convertRestToDay(List<RestDay> restDayList) {
        // подготовим результирующий список
        List<Day> result = new ArrayList<>();

        // если на вход пришёл пустой список (из сети), то сразу вернём пустой
        // ненулевой результирующий список
        if (restDayList == null || restDayList.size() == 0) {
            return result;
        }

        // перебираем все элементы и копируем из них данные в новые модели
        for (RestDay restDay : restDayList) {
            Day day = new Day(); // создаём объект базы данных
            // копируем в него из сетевого объекта данные
            day.setListSchedule(restDay.getListSchedule());
            day.setNameOfDay(restDay.getNameOfDay());
            // готовый объект добавляем в результирующий список
            result.add(day);
        }

        // возвращаем готовый список
        return result;
    }
}
