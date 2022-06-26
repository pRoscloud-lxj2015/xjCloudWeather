package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.BasePresenter
import com.iclound.xjcloudweather.base.DaylyDatas
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.http.ResultHanlderData
import com.iclound.xjcloudweather.mvp.contract.HalfWeatherContract
import com.iclound.xjcloudweather.mvp.model.HalfWeatherModel
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class HalfWeatherPresenter : BasePresenter<HalfWeatherContract.Model, HalfWeatherContract.View>(), HalfWeatherContract.Presenter  {

    override fun createModel(): HalfWeatherContract.Model? = HalfWeatherModel()

    override fun requestHalfWeatherData(location: String) {
        mModel?.requestHalfWeatherData(location)?.ResultHanlderData(mModel, mView){
            mView?.showSevData(it)
        }
//        mModel?.requestHalfWeatherData(location)?.compose(SchedulerUtils.ioToMain())
//            ?.subscribe(object : Observer<DaylyDatas> {
//                override fun onComplete() {
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                    mModel?.addDisposable(d)
//                }
//
//                override fun onNext(t: DaylyDatas) {
//                    mView?.showSevData(t)
//                }
//
//                override fun onError(t: Throwable) {
//                }
//            })
    }

    override fun loadHalfWeatherData(key: String) {
        mModel?.loadHalfWeatherData(key)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<DaylyDatas>(it)?.let { mView?.showSevData(it) }
        }

//            ?.compose(SchedulerUtils.ioToMain())
//            ?.subscribe(object : Observer<CacheEntity> {
//                override fun onSubscribe(d: Disposable) {
//                    mModel?.addDisposable(d)
//                }
//
//                override fun onNext(t: CacheEntity) {
//                    onDeserialiByte<DaylyDatas>(t)?.let { mView?.showSevData(it) }
//                }
//
//                override fun onError(e: Throwable) {
//
//                }
//
//                override fun onComplete() {
//                    TODO("Not yet implemented")
//                }
//
//            })
    }

    //反序列,把二进制数据转换成java object对象
    private fun toObject(data: ByteArray?): Any? {
        val bais = ByteArrayInputStream(data)
        val ois = ObjectInputStream(bais)
        val readObject = ois.readObject()
        ois.close()
        return readObject
    }

    private fun<T> onDeserialiByte(data: Any): T?{
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
}