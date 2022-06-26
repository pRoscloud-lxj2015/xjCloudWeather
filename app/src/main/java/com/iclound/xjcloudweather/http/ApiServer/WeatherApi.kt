package com.iclound.xjcloudweather.http.ApiServer

import com.iclound.xjcloudweather.BuildConfig
import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.http.HttpConstant
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi{
    /**
     * 实时天气
     * https://devapi.qweather.com/v7/weather/now?location=101010100&key=99ad706439a9403189249150a4c8b13f
     * https://devapi.qweather.com/v7/weather/now?location=116.119754,39.917851&key=99ad706439a9403189249150a4c8b13f
     * https://api.qweather.com/v7/weather/3d?location=101010100&key=你的KEY
    // 开发版 https://devapi.qweather.com/v7/weather/3d?location=门头沟&key=99ad706439a9403189249150a4c8b13f
    https://geoapi.qweather.com/v2/city/lookup
    https://geoapi.qweather.com/v2/city/lookup?location=北京&key=99ad706439a9403189249150a4c8b13f
     116.119754,39.917851
    https://geoapi.qweather.com/v2/city/lookup?location=门头沟&adm=北京&key=99ad706439a9403189249150a4c8b13f
    开发版 https://devapi.qweather.com/v7/weather/3d?location=101010100&key=99ad706439a9403189249150a4c8b13f
    https://devapi.qweather.com/v7/weather/24h?location=101010100&key=99ad706439a9403189249150a4c8b13f
    https://geoapi.qweather.com/v2/city/top?number=23&range=cn&key=99ad706439a9403189249150a4c8b13f
    https://geoapi.qweather.com/v2/city/top?number=20&range=cn&key=99ad706439a9403189249150a4c8b13f
     */
    @GET("weather/now")
    fun getRealDataWeather(@Query("location") location: String, @Query("key") key: String= BuildConfig.HeFengKey): Observable<WeatherDatas>

    /**
     * 3天天气数据
     */
    @GET("weather/3d")
    fun getThreeDataWeather(@Query("location") location: String, @Query("key") key: String=BuildConfig.HeFengKey): Observable<DaylyDatas>

    /**
     * 7天气数据
     */
    @GET("weather/7d")
    fun getSevDataWeather(@Query("location") location: String, @Query("key") key: String=BuildConfig.HeFengKey): Observable<DaylyDatas>

    @GET("weather/24h")
    fun getHourlyWeather(@Query("location") location: String, @Query("key") key: String=BuildConfig.HeFengKey): Observable<HourlyDatas>

    @GET("air/now")
    fun getAirNowWeather(@Query("location") location: String, @Query("key") key: String=BuildConfig.HeFengKey): Observable<AirDailyDatas>

    @GET("warning/now")
    fun getWarningNowWeather(@Query("location") location: String, @Query("key") key: String=BuildConfig.HeFengKey): Observable<WarningNowDatas>

    /**
     * 获取搜索城市
     */
    @GET("city/lookup")
    fun getSearchCity(@Query("location") location: String, @Query("key") key: String=BuildConfig.HeFengKey): Observable<SearchCityDatas>

}