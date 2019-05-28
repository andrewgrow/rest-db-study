package com.example.restdbstudy.retrofit;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.restdbstudy.activities.MainActivity;
import com.example.restdbstudy.database.DB;
import com.example.restdbstudy.models.Day;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Логика для работы с сервером
 */
public class Rest {

    private static final String TAG = Rest.class.getSimpleName();
    private static Rest instance; // синглтон-объект класса
    private IRest service; // объект реализующий интерфейс для работы с сетью

    // приватный конструкцтор, запрещающий создание других экземпляров класса
    private Rest() {
        init();
    }

    /**
     * Метод для получения объекта Rest
     */
    public static Rest getInstance() {
        if (instance == null) {
            instance = new Rest();
        }
        return instance;
    }

    /**
     * Непосредственное создание объекта для работы с сетью и его настройка
     */
    private void init() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gahov.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(IRest.class);
    }

    /**
     * Метод для получения списка дней
     * @param callback на вход мы получаем объект, которому сообщим о результате работы с сервером
     */
    public static void getAllDays(MainActivity.ScheduleCallback callback) {
        // получаем объект Rest
        Rest rest = getInstance();
        // создаём вызов, который вернёт от сервиса результат работы метода getAllDays описанного в интерфейсе
        Call<List<RestDay>> call = rest.service.getAllDays();

        // формируем у вызова асинхронную работу (enqueue) с ожиданием результата через коллбек со списком дней
        call.enqueue(new Callback<List<RestDay>>() {

            // вызов коллбека с результатами
            @Override
            public void onResponse(Call<List<RestDay>> call, Response<List<RestDay>> response) {
                // если ответ от сервера 200 ОК
                if (response.code() == 200) {

                    // Конвертацию из RestDay to Day
                    List<RestDay> restDayList = response.body();
                    List<Day> dayList = RestDay.convertRestToDay(restDayList);

                    // новый список расписаний нужно сохранить в базу данных
                    DB.insertOrUpdateIfExists(dayList);

                    // если коллбек на входе метода был не пустой, сообщаем ему о результате
                    if (callback != null) {
                        callback.onCall(dayList);
                    }
                } else {
                    Log.e(TAG, "error! -> response code " + response.code()
                            + ", " + response.message());
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<List<RestDay>> call, Throwable t) {
                Log.e(TAG, "onFailure! -> " + t.getMessage());
                t.printStackTrace();

                // если при выполнении сетевого запроса возникла ошибка, надо попросить у базы
                // кешированный список и вернут ьего коллбеку
                DB.getAllDays()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Day>>() {
                            @Override
                            public void accept(List<Day> dayList) throws Exception {
                                if (callback != null) {
                                    callback.onCall(dayList);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, throwable.getMessage());
                                throwable.printStackTrace();
                            }
                        });
            }
        });
    }
}
