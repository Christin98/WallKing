package com.kcdeveloperss.wallpapers.network;

import android.content.Context;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static Context context;
    private static ApiService REST_CLIENT;

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static ApiService get() {
        return REST_CLIENT;
    }

    public static ApiService post() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.interceptors().add(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient.build())
                .build();

        REST_CLIENT = retrofit.create(ApiService.class);
    }
}
