package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.entity.CacheEntity
import io.reactivex.rxjava3.core.Observable

interface WarningDetailsContract {
    interface View: IView {
        fun showWarningInfo(datas: WarningNowDatas)
    }

    interface Presenter: IPresenter<View> {
        fun getWarningInfo(location: String)
    }

    interface Model : IModel {
        fun getWarningInfo(location: String): Observable<CacheEntity>?
    }
}