package com.example.restdbstudy.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.restdbstudy.models.Day;

import java.util.List;

/**
 * Интерфейс описывающий методы работы с таблицами.
 */
@Dao
public interface IDatabaseAccessObject {

    /**
     * @return список всех дней
     */
    @Query("SELECT * FROM day")
    List<Day> getAll();

    /**
     * @param dayName имя дня недели для выборки из базы данных
     * @return найденный день в базе
     */
    @Query("SELECT * FROM day WHERE nameOfDay = :dayName")
    Day getDayByName(String dayName);

    /**
     * @param day День который необходимо вставить в базу данных
     */
    @Insert
    void insert(Day day);

    /**
     * @param day обновляет день
     */
    @Update
    void update(Day day);

    /**
     * @param day удаляет день
     */
    @Delete
    void delete(Day day);
}
