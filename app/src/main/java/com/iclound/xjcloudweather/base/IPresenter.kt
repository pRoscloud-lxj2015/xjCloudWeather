package com.iclound.xjcloudweather.base

interface IPresenter<in V: IView> {
    fun attachView(view: V)

    fun detachView()
}