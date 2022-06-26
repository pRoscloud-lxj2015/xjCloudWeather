package com.iclound.xjcloudweather.mvp.model

import com.iclound.xjcloudweather.base.WarningNowDatas
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.http.RetrofitHelper
import com.iclound.xjcloudweather.mvp.contract.SearchCityContract
import com.iclound.xjcloudweather.mvp.contract.WarningDetailsContract
import io.reactivex.rxjava3.core.Observable

class WarningDetailsModel : CommonModel(), WarningDetailsContract.Model {

    override fun getWarningInfo(location: String): Observable<CacheEntity>? {
        return AppRepoData.getInstance().getCachetest<CacheEntity>("WarningNowDatas_" +location)
        //return RetrofitHelper.geoservice.getWarningNowWeather(location)
    }
}