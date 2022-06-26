package com.iclound.xjcloudweather.base

import io.reactivex.rxjava3.disposables.Disposable


interface IModel {
    fun addDisposable(disposable: Disposable)

    fun onDetach()
}