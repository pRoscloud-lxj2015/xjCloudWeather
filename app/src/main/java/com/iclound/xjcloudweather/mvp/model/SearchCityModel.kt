package com.iclound.xjcloudweather.mvp.model

import com.iclound.xjcloudweather.base.DaylyDatas
import com.iclound.xjcloudweather.base.SearchCityDatas
import com.iclound.xjcloudweather.http.RetrofitHelper
import com.iclound.xjcloudweather.mvp.contract.SearchCityContract
import io.reactivex.rxjava3.core.Observable

class SearchCityModel : CommonModel(), SearchCityContract.Model {

    override fun getTopCity(cityName: String): Observable<SearchCityDatas> {
        return RetrofitHelper.geoservice.getSearchCity(cityName)
    }

    override fun getSearchCity(cityName: String): Observable<SearchCityDatas> {
        return RetrofitHelper.geoservice.getSearchCity(cityName)
    }

    override fun getSearchCityWeather(location: String): Observable<DaylyDatas> {
        return RetrofitHelper.service.getSevDataWeather(location)
    }
}