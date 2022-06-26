package com.iclound.xjcloudweather.mvp.model

import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import com.iclound.xjcloudweather.mvp.contract.CityManagerContract
import io.reactivex.rxjava3.core.Observable

class CityManagerModel :  CommonModel(), CityManagerContract.Model{

    override fun getCityManagerData(): Observable<List<CityWeatherEntity>> {
        return AppRepoData.getInstance().getCityWeather()
    }

    override fun getCityies(): Observable<List<CityEntity>> {
        return AppRepoData.getInstance().getObsCities()
    }

    override fun updateCityManagerData(weatherDatas: List<CityWeatherEntity>, headData: CityWeatherEntity) {
        AppRepoData.getInstance().removeAllCityWeather()
        AppRepoData.getInstance().addCityWeather(headData)
        weatherDatas.forEach {
            AppRepoData.getInstance().addCityWeather(it)
        }
    }
}