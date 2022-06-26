package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.*
import io.reactivex.rxjava3.core.Observable

interface SearchCityContract {

    interface View: IView {
        fun showTopCity(datas: SearchCityDatas)
        fun showSearchCity(datas: SearchCityDatas)
        fun showSearchCityWeather(datas: DaylyDatas)
    }

    interface Presenter: IPresenter<View> {
        fun getTopCity(cityName: String)
        fun getSearchCity(cityName: String)
        fun getSearchCityWeather(location: String)
    }

    interface Model : IModel {
        fun getTopCity(cityName: String): Observable<SearchCityDatas>
        fun getSearchCity(cityName: String): Observable<SearchCityDatas>
        fun getSearchCityWeather(location: String): Observable<DaylyDatas>
    }
}