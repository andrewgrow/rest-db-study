package com.example.restdbstudy.database;

import android.arch.persistence.room.RoomDatabase;

import com.example.restdbstudy.models.Day;

/**
 * Менеджер для получения различных ДАО и общей конфигурации базы данных. Наследник RoomDatabase
 */
@android.arch.persistence.room.Database(entities =  {Day.class}, version = 1, exportSchema = false)
public abstract class DatabaseManager extends RoomDatabase {
    public abstract IDatabaseAccessObject getDAO();
}
