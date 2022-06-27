package com.example.hindware.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.example.hindware.utility.QwikcilverAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by SandeepY on 08122020
 **/

public class RestClient {

    public static Retrofit getClient() {
        return RetrofitAPI.retrofit;
    }

    private static class RetrofitAPI {
        private static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                // .addInterceptor(BuildConfig.DEBUG ?
                // httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY) :
                // httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE))
                .retryOnConnectionFailure(true)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build();

        private static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(QwikcilverAPI.getBaseUrl())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
