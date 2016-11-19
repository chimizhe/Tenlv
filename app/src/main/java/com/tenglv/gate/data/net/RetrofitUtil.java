package com.tenglv.gate.data.net;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Description
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2015-11-25 16:28)
 */
public class RetrofitUtil {


    private static final String TEST_API = "http://119.29.248.108:3000/";
    private static final String RELEASE_API = "http://facecheck.51ekt.com:3000/";


    private static final String API_BASE_URL = RELEASE_API;


    private static Retrofit sRetrofit;
    private static Retrofit sRetrofit2;
    private static OkHttpClient sClient;
    private static OkHttpClient sClient2;

    static {

        sClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();

        sClient2 = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();

        sRetrofit = new Retrofit.Builder().baseUrl(API_BASE_URL)
                .client(sClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        sRetrofit2 = new Retrofit.Builder().baseUrl(API_BASE_URL)
                .client(sClient2)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();


    }


    public static <S> S createService(Class<S> serviceClass) {
        return sRetrofit.create(serviceClass);
    }

    public static <S> S createService2(Class<S> serviceClass) {
        return sRetrofit2.create(serviceClass);
    }

}