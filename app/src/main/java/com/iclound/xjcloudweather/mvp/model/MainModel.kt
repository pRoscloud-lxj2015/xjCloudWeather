package com.iclound.xjcloudweather.mvp.model

import com.iclound.xjcloudweather.base.BaseModel
import com.iclound.xjcloudweather.base.Location
import com.iclound.xjcloudweather.base.WeatherDatas
import com.iclound.xjcloudweather.http.GaodeMapHelper
import com.iclound.xjcloudweather.http.RetrofitHelper
import com.iclound.xjcloudweather.mvp.contract.MainContract
import io.reactivex.rxjava3.core.Observable

class MainModel : BaseModel(), MainContract.Model{

    override fun requestRealData(location: String): Observable<WeatherDatas> {
        return RetrofitHelper.service.getRealDataWeather(location)
    }

}