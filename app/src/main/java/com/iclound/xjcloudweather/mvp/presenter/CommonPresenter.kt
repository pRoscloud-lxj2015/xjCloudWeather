package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.BasePresenter
import com.iclound.xjcloudweather.mvp.contract.CommonContract

open class CommonPresenter<M: CommonContract.Model, V: CommonContract.View>: BasePresenter<M, V>(), CommonContract.Presenter<V> {

}