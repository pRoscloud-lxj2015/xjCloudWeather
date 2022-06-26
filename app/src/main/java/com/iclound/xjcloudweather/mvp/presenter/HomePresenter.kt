package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.event.MapLocationEvent
import com.iclound.xjcloudweather.http.ResultHanlderData
import com.iclound.xjcloudweather.mvp.contract.HomeContract
import com.iclound.xjcloudweather.mvp.model.HomeModel
import com.iclound.xjcloudweather.mvp.model.bean.RealDataWeather
import com.iclound.xjcloudweather.scheduler.SchedulerUtils
import com.tencent.mmkv.MMKV
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_weather_layout.*
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream


class HomePresenter : CommonPresenter<HomeContract.Model, HomeContract.View>(), HomeContract.Presenter {

    override fun createModel(): HomeContract.Model? = HomeModel()

    //反序列,把二进制数据转换成java object对象
    fun toObject(data: ByteArray?): Any? {
        val bais = ByteArrayInputStream(data)
        val ois = ObjectInputStream(bais)
        val readObject = ois.readObject()
        ois.close()
        return readObject
    }

    fun<T> onDeserialiByte(data: Any): T?{
        val cache = data as CacheEntity
        return if (cache.data != null) {
            if (cache.dead_line == 0L) {
                toObject(cache.data) as T
            } else {
                if (cache.dead_line > System.currentTimeMillis() / 1000) {
                    toObject(cache.data) as T
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    override fun loadCacheRealData(key: String) {
        mModel?.loadCacheRealData(key)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<WeatherDatas>(it)?.let { mView?.showFragment(it) }
        }
    }

    override fun loadCacheDayData(key: String) {
        mModel?.loadCacheDayData(key)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<DaylyDatas>(it)?.let { mView?.showThreeDayFragment(it) }
        }
    }

    override fun loadCacheHourlyData(key: String) {
        mModel?.loadCacheHourlyData(key)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<HourlyDatas>(it)?.let { mView?.showHourlyFragment(it) }
        }
    }

    override fun loadCacheAirNowData(key: String) {
        mModel?.loadCacheAirNowData(key)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<AirDailyDatas>(it)?.let { mView?.showAirNow(it) }
        }
    }

    override fun loadCacheWarningNowData(key: String) {
        mModel?.loadCacheWarningNowData(key)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<WarningNowDatas>(it)?.let { mView?.showWarningNow(it) }
        }
    }

    //合并天气数据请求
    override fun requestWeatherData(location: String) {
//        mModel?.requestWeatherData(location)?.ResultHanlderData(mModel, mView) {
//            when (it) {
//                is WeatherDatas -> {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        AppRepoData.getInstance().saveCache("WeatherDatas_" + location, it)
//                    }
//                    mView?.showFragment(it)
//                }
//                is DaylyDatas -> {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        AppRepoData.getInstance().saveCache("DaylyDatas_" + location, it)
//                    }
//                    mView?.showThreeDayFragment(it)
//                }
//                is HourlyDatas -> {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        AppRepoData.getInstance().saveCache("HourlyDatas_" + location, it)
//                    }
//                    mView?.showHourlyFragment(it)
//                }
//                is AirDailyDatas -> {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        AppRepoData.getInstance().saveCache("AirDailyDatas_" + location, it)
//                    }
//                    mView?.showAirNow(it)
//                }
//                is WarningNowDatas -> {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        AppRepoData.getInstance().saveCache("WarningNowDatas_" + location, it)
//                    }
//                    mView?.showWarningNow(it)
//                }
//                else -> {}
//            }
//        }
        MMKV.defaultMMKV().encode(location, System.currentTimeMillis())
        println("MMKV encode" + " location:" + location + " value:"+ MMKV.defaultMMKV().decodeLong(location))
        mModel?.requestWeatherData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<Any> {
                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }
                override fun onNext(t: Any) {
                    when(t){
                        is WeatherDatas ->{
                            //GlobalScope.launch(Dispatchers.IO){
                                AppRepoData.getInstance().saveCache("WeatherDatas_" + location, t)
                           // }
                            mView?.showFragment(t)}
                        is DaylyDatas ->{
                            //GlobalScope.launch(Dispatchers.IO){
                                AppRepoData.getInstance().saveCache("DaylyDatas_" + location, t)
                           // }
                            mView?.showThreeDayFragment(t)}
                        is HourlyDatas ->{
                            //GlobalScope.launch(Dispatchers.IO){
                                AppRepoData.getInstance().saveCache("HourlyDatas_" + location, t)
                            //}
                            mView?.showHourlyFragment(t)}
                        is AirDailyDatas->{
                            //GlobalScope.launch(Dispatchers.IO){
                                AppRepoData.getInstance().saveCache("AirDailyDatas_" + location, t)
                           // }
                            mView?.showAirNow(t)}
                        is WarningNowDatas -> {
                            //GlobalScope.launch(Dispatchers.IO){
                                AppRepoData.getInstance().saveCache("WarningNowDatas_" + location, t)
                           // }
                            mView?.showWarningNow(t)}
                    }
                }
                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                    mView?.finishRequest()
                    mModel?.onDetach()
                }

            })
    }

    override fun requestRealData(location: String) {
        mModel?.requestRealData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<WeatherDatas> {
                override fun onComplete() {
                    mModel?.onDetach()
                }

                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }

                override fun onNext(t: WeatherDatas) {
                    mView?.showFragment(t)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun requestDayByData() {
        //TODO("Not yet implemented")
    }

    override fun requestThreeDayData(location: String) {
        mModel?.requestThreeDayData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<DaylyDatas> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }

                override fun onNext(t: DaylyDatas) {
                    mView?.showThreeDayFragment(t)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun requestHourData(location: String) {
        mModel?.requestHourlyData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<HourlyDatas> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }

                override fun onNext(t: HourlyDatas) {
                    mView?.showHourlyFragment(t)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun requestAirNowData(location: String) {
        mModel?.requestAirNowData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<AirDailyDatas> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }

                override fun onNext(t: AirDailyDatas) {
                    mView?.showAirNow(t)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun requestWarningNowData(location: String) {
        mModel?.requestWarningNowData(location)?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : Observer<WarningNowDatas> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    mModel?.addDisposable(d)
                }

                override fun onNext(t: WarningNowDatas) {
                    mView?.showWarningNow(t)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun requestHalfMouseData() {

    }


}
