package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.entity.CacheEntity
import io.reactivex.rxjava3.core.Observable

interface HalfWeatherContract {

    interface View: IView {
        fun showSevData(datas: DaylyDatas)
    }

    interface Presenter: IPresenter<View> {
        fun requestHalfWeatherData(location: String)
        fun loadHalfWeatherData(key: String)
    }

    interface Model : IModel {
        fun requestHalfWeatherData(location: String): Observable<DaylyDatas>
        fun loadHalfWeatherData(key: String): Observable<CacheEntity>?
    }

}