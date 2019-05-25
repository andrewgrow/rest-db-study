package com.example.teststreaming.retrofit;

import com.example.teststreaming.models.Day;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRest {

    @GET("schedule")
    public Call<List<Day>> getAllDays();

    @GET("schedule")
    public Call<List<Day>> getDay(
            @Query("day") String dayName
    );
}
