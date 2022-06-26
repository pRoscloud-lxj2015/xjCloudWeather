package com.iclound.xjcloudweather.http

import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.Location
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.event.MapLocationEvent
import com.iclound.xjcloudweather.utils.DataUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

object GaodeMapHelper {

    fun getCurrentLocation(first: Boolean = false) {
        val mLocationClient = AMapLocationClient(App.instance.applicationContext)
        //初始化AMapLocationClientOption对象
        val mLocationOption = AMapLocationClientOption()
        mLocationOption.run {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            httpTimeOut = 30000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            interval = 2000//可选，设置定位间隔。默认为2秒
            isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
            isOnceLocation = false//可选，设置是否单次定位。默认是false
            isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            isSensorEnable = false//可选，设置是否使用传感器。默认是false
            isWifiScan = true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
            setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT)//可选，
        }
        //var location: Location? = null
        mLocationClient.run{
            setLocationListener{ aMapLocation ->
                if(aMapLocation.errorCode == 0){
                    val city = aMapLocation.city
                    val cityId = aMapLocation.cityCode
                    val lat = aMapLocation.latitude
                    val lon = aMapLocation.longitude
                    val dis = aMapLocation.district
                    val street = aMapLocation.street
                    //location = Location(city, cityId, lat.toString(), lon.toString(), dis, street)
                    mLocationClient?.stopLocation()
                    var name:String? = null
                    name = if(dis.isEmpty()){
                        city +" " + street
                    }else
                        dis + " " + street
                    val location = DataUtils.getNoMoreThanTwoDigits(aMapLocation?.longitude) + "," +DataUtils.getNoMoreThanTwoDigits(aMapLocation?.latitude)

                    val curLoc = MMKV.defaultMMKV().decodeString("location-loc")
                    if(curLoc == null){
                            MMKV.defaultMMKV().encode("location-loc", location)
                            AppRepoData.getInstance().addCity(CityEntity(location, name, true ))
                    }else{
                        if(location != curLoc){
                                MMKV.defaultMMKV().encode("location-loc", curLoc)
                                AppRepoData.getInstance().removeCity(curLoc)
                                AppRepoData.getInstance().addCity(CityEntity(location, name, true ))
                        }
                    }



                    //GlobalScope.launch(Dispatchers.IO){

                 //   }
                    Log.d("", "定位成功：" + location)
                    //EventBus.getDefault().post(MapLocationEvent(location))
                }else{
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation?.errorCode + ", errInfo:"
                            + aMapLocation?.errorInfo)
                }
            }
            //给定位客户端对象设置定位参数
            setLocationOption(mLocationOption);
            startLocation(); //启动定位
        }
    }
}