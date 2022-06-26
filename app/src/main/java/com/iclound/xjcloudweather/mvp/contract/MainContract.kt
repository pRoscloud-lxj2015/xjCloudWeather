package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.*
import io.reactivex.rxjava3.core.Observable

interface MainContract {

    interface View: IView {
        fun showFragment(datas: WeatherDatas)
    }

    interface Presenter: IPresenter<View> {
        fun requestRealData(location: String)
    }

    interface Model : IModel {
        fun requestRealData(location: String): Observable<WeatherDatas>
    }


}