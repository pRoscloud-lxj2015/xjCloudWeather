package com.iclound.xjcloudweather.utils

import android.util.Log
import com.tencent.mmkv.MMKV

object RefreshUtils {

    fun hasRefreshData(location: String): Boolean{
        val mmkv = MMKV.defaultMMKV()
        val timeMills = mmkv.decodeLong(location)
        val curMills = System.currentTimeMillis()
        Log.d("", "MMKV decode" + " location:" + location + " value:"+ timeMills)
        return if( timeMills != 0L){
            curMills - timeMills > ContentUtil.REFRESH_WEATHER_DATA_TIME
        }else
            true
    }

    fun isAlreadyWeatherData(cityId: String): Boolean{
        val mmkv = MMKV.defaultMMKV()
        val timeMills = mmkv.decodeLong(cityId)
        return timeMills > 0
    }

}