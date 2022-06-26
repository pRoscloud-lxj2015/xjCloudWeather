package com.iclound.xjcloudweather.mvp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.http.RetrofitHelper
import com.iclound.xjcloudweather.mvp.contract.HomeContract
import com.iclound.xjcloudweather.mvp.model.bean.DataWeather
import com.iclound.xjcloudweather.mvp.model.bean.RealDataWeather
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

class HomeModel : CommonModel(), HomeContract.Model {

    override fun requestWeatherData(location: String): Observable<Any> {
        return Observable.mergeArray( RetrofitHelper.service.getRealDataWeather(location),
                            RetrofitHelper.service.getSevDataWeather(location),
                            RetrofitHelper.service.getHourlyWeather(location),
                            RetrofitHelper.service.getAirNowWeather(location),
                            RetrofitHelper.service.getWarningNowWeather(location))
    }

    override fun loadCacheWeatherData(key: String): Observable<Any> {
        return Observable.concatArray(AppRepoData.getInstance().getCachetest<WeatherDatas>("WeatherDatas_" + key),
            AppRepoData.getInstance().getCachetest<DaylyDatas>("DaylyDatas_" +key),
            AppRepoData.getInstance().getCachetest<HourlyDatas>("HourlyDatas_" +key),
            AppRepoData.getInstance().getCachetest<AirDailyDatas>("AirDailyDatas_" +key),
            AppRepoData.getInstance().getCachetest<WarningNowDatas>("WarningNowDatas_" +key))
    }

    override fun loadCacheRealData(key: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<WeatherDatas>("WeatherDatas_" + key)
    }

    override fun loadCacheDayData(key: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<DaylyDatas>("DaylyDatas_" +key)
    }

    override fun loadCacheHourlyData(key: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<HourlyDatas>("HourlyDatas_" +key)
    }

    override fun loadCacheAirNowData(key: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<AirDailyDatas>("AirDailyDatas_" +key)
    }

    override fun loadCacheWarningNowData(key: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<WarningNowDatas>("WarningNowDatas_" +key)
    }

    override fun requestRealData(location: String): Observable<WeatherDatas> {
        return RetrofitHelper.service.getRealDataWeather(location)
    }

    override fun requestThreeDayData(location: String): Observable<DaylyDatas> {
        return RetrofitHelper.service.getThreeDataWeather(location)
    }

    override fun requestHourlyData(location: String): Observable<HourlyDatas> {
        return RetrofitHelper.service.getHourlyWeather(location)
    }

    override fun requestAirNowData(location: String): Observable<AirDailyDatas> {
       return  RetrofitHelper.service.getAirNowWeather(location)
    }

    override fun requestWarningNowData(location: String): Observable<WarningNowDatas> {
        return RetrofitHelper.service.getWarningNowWeather(location)
    }

}