package com.iclound.xjcloudweather.db

import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.db.dao.CacheDao
import com.iclound.xjcloudweather.db.dao.CityDao
import com.iclound.xjcloudweather.db.dao.CityWeatherDao
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class AppRepoData {
    private val cacheDao: CacheDao = AppDataBase.getInstance(App.context).cacheDao()

    private val cityDao: CityDao = AppDataBase.getInstance(App.context).cityDao()
    private val cityWeatherDao: CityWeatherDao = AppDataBase.getInstance(App.context).cityWeatherDao()

    fun addCity(city: CityEntity){
        cityDao.addCity(city)
    }

     fun removeCity(cityId: String) {
        cityDao.removeCity(cityId)
    }

     fun removeAllCity() {
        cityDao.removeAllCity()
    }

     fun getCities(): List<CityEntity> {
        return cityDao.getCities()
    }

    fun getObsCities(): Observable<List<CityEntity>>{
        return cityDao.getObsCities()
    }

    fun addCityWeather(weather: CityWeatherEntity){
        cityWeatherDao.addCityWeather(weather)
    }

    fun getCityWeather() : Observable<List<CityWeatherEntity>> {
        return cityWeatherDao.getCityWeathers()
    }

    fun getCityWeater(): List<CityWeatherEntity>{
        return cityWeatherDao.getCityWeatherstest()
    }

    fun removeCityWeather(weather: String) {
        cityWeatherDao.removeCityWeather(weather)
    }

    fun removeAllCityWeather(){
        return cityWeatherDao.removeAllCityWeathers()
    }


     fun deleteCache(key: String) {
        val cache = CacheEntity()
        cache.key = key
        cacheDao.deleteCache(cache)
    }

    fun <T> saveCache(key: String, body: T) {
        val saveTime: Int = 0
        val cache = CacheEntity()
        cache.key = key
        cache.data = toByteArray(body)
        if (saveTime == 0) {
            cache.dead_line = 0
        } else {
            cache.dead_line = System.currentTimeMillis() / 1000 + saveTime
        }
        cacheDao.saveCache(cache)
    }

    fun <T> getCache(key: String): T?{
        val cache: CacheEntity? = cacheDao.getCache(key)
        return if (cache?.data != null) {
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

    fun <T> getCachetest(key: String): Observable<CacheEntity>? {
        val cache: Observable<CacheEntity>? = cacheDao.getCachetest(key)
        return cache
//        val cache: Observable<CacheEntity>? = cacheDao.getCachetest(key)
//        return if (cache?.data != null) {
//            if (cache.dead_line == 0L) {
//                toObject(cache.data) as Observable<T>
//            } else {
//                if (cache.dead_line > System.currentTimeMillis() / 1000) {
//                    toObject(cache.data) as Observable<T>
//                } else {
//                    null
//                }
//            }
//        } else {
//            null
//        }
    }

    fun getCacheEmpty(key: String): Observable<Boolean>?{
        val cache: Observable<Boolean>? = cacheDao.getCacheEmpty(key)
        return cache
    }

    //序列化存储数据需要转换成二进制
    private fun <T> toByteArray(body: T): ByteArray {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(body)
        oos.flush()
        oos.close()
        return baos.toByteArray()
    }

    //反序列,把二进制数据转换成java object对象
    private fun toObject(data: ByteArray?): Any? {
        val bais = ByteArrayInputStream(data)
        val ois = ObjectInputStream(bais)
        val readObject = ois.readObject()
        ois.close()
        return readObject
    }

    companion object {
        @Volatile
        private var instance: AppRepoData? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: AppRepoData()
                        .also { instance = it }
            }
    }
}