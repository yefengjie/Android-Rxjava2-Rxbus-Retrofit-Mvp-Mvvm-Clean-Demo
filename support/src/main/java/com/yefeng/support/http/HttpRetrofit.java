package com.yefeng.support.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yefeng.support.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by yefeng on 21/01/2017.
 * retrofit instance
 */

public class HttpRetrofit {

    private static Retrofit mRetrofit;

    private HttpRetrofit() {
    }

    private static class SingletonHolder {
        private static final HttpRetrofit INSTANCE = new HttpRetrofit();
    }

    public static HttpRetrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Retrofit getRetrofit() {
        Timber.d("method: %s, thread: %s_%s", "getRetrofit()", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (null == mRetrofit) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            // print log
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addInterceptor(logging);
            }
            // set time out
            httpClientBuilder.readTimeout(Http.getTimeOut(), TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(Http.getTimeOut(), TimeUnit.SECONDS);
            // add request params
            httpClientBuilder.addInterceptor(HttpRequestInterceptor.getInstance());

            OkHttpClient httpClient = httpClientBuilder.build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Http.host())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return mRetrofit;
    }

    public <T> T getService(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }
}
