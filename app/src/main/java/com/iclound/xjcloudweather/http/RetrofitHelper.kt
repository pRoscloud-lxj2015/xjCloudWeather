package com.iclound.xjcloudweather.http

import com.iclound.xjcloudweather.BuildConfig
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.http.ApiServer.WeatherApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private var retrofit: Retrofit? = null
    private var retrofitgeo: Retrofit? = null

    val service: WeatherApi by lazy { getRetrofit()!!.create(WeatherApi::class.java)}

    val geoservice: WeatherApi by lazy { getGeoRetrofit()!!.create(WeatherApi::class.java)}


    private fun getRetrofit(): Retrofit?{
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(HttpConstant.BASE_URL)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        return retrofit
    }

    private fun getGeoRetrofit(): Retrofit?{
        if(retrofitgeo == null){
            retrofitgeo = Retrofit.Builder()
                .baseUrl(HttpConstant.BASE_GEO_URL)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        return retrofitgeo
    }

//https://devapi.qweather.com/v7/weather/now?location=101010100&key=99ad706439a9403189249150a4c8b13f
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        builder.run{
            addInterceptor(httpLoggingInterceptor)
            cache(Cache(App.context.cacheDir, 10*1024))
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            build()
        }
        return builder.build()
    }
}
