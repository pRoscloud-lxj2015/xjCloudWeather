package com.iclound.xjcloudweather.mvp.model

import com.iclound.xjcloudweather.base.DaylyDatas
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.http.RetrofitHelper
import com.iclound.xjcloudweather.mvp.contract.HalfWeatherContract
import io.reactivex.rxjava3.core.Observable

class HalfWeatherModel : CommonModel(), HalfWeatherContract.Model{
    override fun requestHalfWeatherData(location: String): Observable<DaylyDatas> {
        return RetrofitHelper.service.getSevDataWeather(location)
    }

    override fun loadHalfWeatherData(key: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<DaylyDatas>("DaylyDatas_" +key)
    }

}