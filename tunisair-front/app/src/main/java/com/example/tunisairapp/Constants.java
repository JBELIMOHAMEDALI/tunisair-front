package com.example.tunisairapp;

import com.example.tunisairapp.models.IapiConnexion;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {
    public static final String SHARED_PREFERENCE_NAME="SHARED_PREF_NAME";
    public final static long SPLASH_SCREEN = 4000;
    public static final String USER_CONNECTED="USER_CONNECTION";
    public static final String IP_ADRESS="192.168.1.2";
    public static final String USER_MAIL="USER_MAIL";
    public static final String USER_MATRICULE="USER_MATRICULE";


    public static Retrofit initRetrofit(String BASE_URL){
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

       return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

}
/*
    }*/