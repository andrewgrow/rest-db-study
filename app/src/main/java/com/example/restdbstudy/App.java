package com.example.restdbstudy;

import android.app.Application;
import android.util.Log;

/**
 * Главный класс приложения, который вызывается при создании приложения.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application is created");
    }
}
