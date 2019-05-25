package com.example.restdbstudy.database;

import android.arch.persistence.room.RoomDatabase;

import com.example.restdbstudy.models.Day;

/**
 * Объект для получения различных ДАО и общей конфигурации базы данных. Наследник RoomDatabase
 */
@android.arch.persistence.room.Database(entities =  {Day.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract IDatabaseAccessObject getDAO();
}
