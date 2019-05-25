package com.example.restdbstudy.retrofit;

import com.example.restdbstudy.models.Day;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Описывает методы работы с сетью
 */
public interface IRest {

    @GET("schedule")
    public Call<List<Day>> getAllDays();

    @GET("schedule")
    public Call<List<Day>> getDay(
            @Query("day") String dayName
    );
}
