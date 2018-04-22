package com.example.kalas.backingapp.api;

import com.example.kalas.backingapp.utils.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kalas on 4/1/2018.
 */

public class ApiClient {

    private static Retrofit retrofit = null;
    public static Retrofit getClient(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Create HttpLoggingInterceptor object and set logging level
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC); // BASIC prints request methods and response codes

        // Couple OkHttpClient.Builder object with HttpLoggingInterceptor object and set connection timeout duration
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.connectTimeout(BuildConfig.CONNECTION_DURATION_TIMEOUT, TimeUnit.MILLISECONDS);

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }

        return retrofit;
    }
}
