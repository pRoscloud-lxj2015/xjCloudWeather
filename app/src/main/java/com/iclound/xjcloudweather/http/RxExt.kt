package com.iclound.xjcloudweather.http

import android.util.Log
import com.iclound.xjcloudweather.base.IModel
import com.iclound.xjcloudweather.base.IView
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import com.iclound.xjcloudweather.scheduler.SchedulerUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


fun <T> Observable<T>?.ResultHanlderData(model: IModel?, view: IView?, function: (T) -> Unit?) {
    this?.compose(SchedulerUtils.ioToMain())?.subscribe(object: Observer<T>{
        override fun onSubscribe(d: Disposable) {
            model?.addDisposable(d)
            Log.d("","ResultHandlerData:" + "onSubscribe" + " " +d.javaClass.name)
        }

        override fun onNext(t: T) {
            function.invoke(t)
            Log.d("","ResultHandlerData:" + "onNext" + " " + Thread.currentThread().getId() + function.toString())
        }

        override fun onError(e: Throwable) {
            Log.d("","ResultHandlerData:" + "onError" + " " + e.printStackTrace())
        }

        override fun onComplete() {
            Log.d("","ResultHandlerData:" + "onComplete")
        }

    })
}

fun <T> Observable<T>?.ResultHanlderAndFinishData(model: IModel?, view: IView?, function: (T) -> Unit?, function2: () -> Unit) {
    this?.compose(SchedulerUtils.ioToMain())?.subscribe(object: Observer<T>{
        override fun onSubscribe(d: Disposable) {
            model?.addDisposable(d)
        }

        override fun onNext(t: T) {
            function.invoke(t)
        }

        override fun onError(e: Throwable) {

        }

        override fun onComplete() {
            function2.invoke()
        }

    })
}



