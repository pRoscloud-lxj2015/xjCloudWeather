package com.iclound.xjcloudweather.mvp.presenter

import com.iclound.xjcloudweather.base.BasePresenter
import com.iclound.xjcloudweather.base.WarningNowDatas
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.http.ResultHanlderData
import com.iclound.xjcloudweather.mvp.contract.WarningDetailsContract
import com.iclound.xjcloudweather.mvp.model.WarningDetailsModel
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class WarningDetailsPresenter : BasePresenter<WarningDetailsContract.Model, WarningDetailsContract.View>(), WarningDetailsContract.Presenter{

    override fun createModel(): WarningDetailsContract.Model? = WarningDetailsModel()

    override fun getWarningInfo(location: String) {
        mModel?.getWarningInfo(location)?.ResultHanlderData(mModel, mView){
            onDeserialiByte<WarningNowDatas>(it)?.let { mView?.showWarningInfo(it) }
        }

    }

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
}