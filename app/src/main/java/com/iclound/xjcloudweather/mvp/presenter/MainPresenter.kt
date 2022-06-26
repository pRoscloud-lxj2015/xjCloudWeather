package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.BasePresenter
import com.iclound.xjcloudweather.base.WeatherDatas
import com.iclound.xjcloudweather.mvp.contract.MainContract
import com.iclound.xjcloudweather.mvp.model.MainModel
import com.iclound.xjcloudweather.scheduler.SchedulerUtils
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MainPresenter : BasePresenter<MainContract.Model, MainContract.View>(), MainContract.Presenter  {

    override fun createModel(): MainContract.Model? = MainModel()

    override fun requestRealData(location: String){
        mModel?.requestRealData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<WeatherDatas> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }

                override fun onNext(t: WeatherDatas) {
                    mView?.showFragment(t)
                    mModel?.onDetach()
                }

                override fun onError(t: Throwable) {
                }
            })
    }

}