package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.BasePresenter
import com.iclound.xjcloudweather.http.ResultHanlderData
import com.iclound.xjcloudweather.mvp.contract.SearchCityContract
import com.iclound.xjcloudweather.mvp.model.SearchCityModel

class SearchCityPresenter : BasePresenter<SearchCityContract.Model, SearchCityContract.View>(), SearchCityContract.Presenter {

    override fun createModel(): SearchCityContract.Model? = SearchCityModel()

    override fun getTopCity(cityName: String) {
        mModel?.getTopCity(cityName)?.ResultHanlderData(mModel, mView){
            mView?.showTopCity(it)
        }
    }

    override fun getSearchCity(cityName: String) {
        mModel?.getSearchCity(cityName)?.ResultHanlderData(mModel, mView){
            mView?.showSearchCity(it)
        }
    }

    override fun getSearchCityWeather(location: String) {
        mModel?.getSearchCityWeather(location)?.ResultHanlderData(mModel, mView){
            mView?.showSearchCityWeather(it)
        }
    }
}