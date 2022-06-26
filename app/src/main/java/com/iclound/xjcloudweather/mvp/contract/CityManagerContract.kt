package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.DaylyDatas
import com.iclound.xjcloudweather.base.IModel
import com.iclound.xjcloudweather.base.IPresenter
import com.iclound.xjcloudweather.base.IView
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import io.reactivex.rxjava3.core.Observable

interface CityManagerContract {

    interface View: IView {
        fun showCityManager(data: List<CityWeatherEntity>)
        fun finishCities(datas: List<CityEntity>)
        fun finishSwapCities(data: List<CityWeatherEntity>)
    }

    interface Presenter: IPresenter<View> {
        fun getCityManagerData()

        fun getCities()

        fun updateCityManagerData(weatherDatas: List<CityWeatherEntity>, headData: CityWeatherEntity)
    }

    interface Model : IModel {
        fun getCityManagerData(): Observable<List<CityWeatherEntity>>

        fun getCityies(): Observable<List<CityEntity>>

        fun updateCityManagerData(weatherDatas: List<CityWeatherEntity>, headData: CityWeatherEntity)
    }
}