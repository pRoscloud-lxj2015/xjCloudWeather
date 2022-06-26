package com.iclound.xjcloudweather.mvp.contract

import com.iclound.xjcloudweather.base.IModel
import com.iclound.xjcloudweather.base.IPresenter
import com.iclound.xjcloudweather.base.IView

interface CommonContract<T, U> {
    interface View: IView {

    }

    interface Presenter<in V: View>: IPresenter<V>{

    }

    interface Model : IModel {

    }
}