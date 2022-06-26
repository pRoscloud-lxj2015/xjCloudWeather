package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.BasePresenter
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import com.iclound.xjcloudweather.http.ResultHanlderData
import com.iclound.xjcloudweather.mvp.contract.CityManagerContract
import com.iclound.xjcloudweather.mvp.model.CityManagerModel

class CityManagerPresenter : BasePresenter<CityManagerContract.Model, CityManagerContract.View>(), CityManagerContract.Presenter {

    override fun createModel(): CityManagerContract.Model? = CityManagerModel()

    override fun getCities() {
        mModel?.getCityies()?.ResultHanlderData(mModel, mView){
            mView?.finishCities(it)
        }
    }

    override fun updateCityManagerData(weatherDatas: List<CityWeatherEntity>, headData: CityWeatherEntity) {
        mModel?.updateCityManagerData(weatherDatas, headData)
        mView?.finishSwapCities(weatherDatas)
        //mModel?.onDetach()
    }

    override fun getCityManagerData() {
        mModel?.getCityManagerData()?.ResultHanlderData(mModel, mView){
            mView?.showCityManager(it)
        }

//        mModel?.getCityManagerData()?.compose(SchedulerUtils.ioToMain())
//            ?.subscribe(object: Observer<List<CityWeatherEntity>>{
//                override fun onSubscribe(d: Disposable) {
//                    mModel?.addDisposable(d)
//                }
//
//                override fun onError(e: Throwable) {
//
//                }
//
//                override fun onComplete() {
//
//                }
//
//                override fun onNext(t: List<CityWeatherEntity>) {
//                    mView?.showCityManager(t)
//                }
//
//            })
    }



}

