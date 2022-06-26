package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.entity.CacheEntity
import io.reactivex.rxjava3.core.Observable

interface HomeContract {
    interface View: CommonContract.View{
        fun showFragment(datas: WeatherDatas)
        fun showThreeDayFragment(datas: DaylyDatas)
        fun showHourlyFragment(datas: HourlyDatas)
        fun showAirNow(datas: AirDailyDatas)
        fun showWarningNow(datas: WarningNowDatas)
        fun finishRequest()
    }

    interface Presenter: CommonContract.Presenter<View>{
        fun requestRealData(location: String)
        fun requestDayByData()
        fun requestThreeDayData(location: String)
        fun requestHourData(location: String)
        fun requestAirNowData(location: String)
        fun requestWarningNowData(location: String)
        fun requestHalfMouseData()
        fun requestWeatherData(location: String)
        fun loadCacheRealData(key: String)
        fun loadCacheDayData(key: String)
        fun loadCacheHourlyData(key: String)
        fun loadCacheAirNowData(key: String)
        fun loadCacheWarningNowData(key: String)
    }

    interface Model : CommonContract.Model{
        fun requestWeatherData(location: String): Observable<Any>
        fun loadCacheWeatherData(key: String): Observable<Any>
        fun loadCacheRealData(key: String): Observable<CacheEntity>?
        fun loadCacheDayData(key: String): Observable<CacheEntity>?
        fun loadCacheHourlyData(key: String): Observable<CacheEntity>?
        fun loadCacheAirNowData(key: String): Observable<CacheEntity>?
        fun loadCacheWarningNowData(key: String): Observable<CacheEntity>?


        fun requestRealData(location: String): Observable<WeatherDatas>
        fun requestThreeDayData(location: String): Observable<DaylyDatas>
        fun requestHourlyData(location: String): Observable<HourlyDatas>
        fun requestAirNowData(location: String): Observable<AirDailyDatas>
        fun requestWarningNowData(location: String): Observable<WarningNowDatas>
    }
}