package com.example.restdbstudy;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.example.restdbstudy.database.Database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Главный класс приложения, который вызывается при создании приложения.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static App instance;
    private static Gson gson;
    private Database db;

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application is created");

        // см. работу с базой данных на уроке в стартандроид
        // https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/529-urok-5-room-osnovy.html
        db =  Room.databaseBuilder(getApplicationContext(),
                Database.class, "database")
                .build();
    }

    public static Database getDatabase() {
        return instance.db;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }
}
