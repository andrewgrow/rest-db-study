package com.example.teststreaming.retrofit;

import android.util.Log;

import com.example.teststreaming.activities.MainActivity;
import com.example.teststreaming.models.Day;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Rest {

    private static final String TAG = Rest.class.getSimpleName();
    private static Rest instance;
    private IRest service;

    private Rest() {
        // private constructor
        init();
    }

    public static Rest getInstance() {
        if (instance == null) {
            instance = new Rest();
        }
        return instance;
    }

    private void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gahov.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IRest.class);
    }

    public static void getAllDays(MainActivity.ScheduleCallback callback) {
        Rest rest = getInstance();
        Call<List<Day>> call = rest.service.getAllDays();
        call.enqueue(new Callback<List<Day>>() {
            @Override
            public void onResponse(Call<List<Day>> call, Response<List<Day>> response) {
                if (response.code() == 200) {
                    if (callback != null) {
                        callback.onCall(response.body());
                    }
                } else {
                    Log.e(TAG, "error! response code " + response.code()
                            + ", " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Day>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
