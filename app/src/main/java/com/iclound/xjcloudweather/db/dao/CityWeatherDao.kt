package com.iclound.xjcloudweather.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import io.reactivex.rxjava3.core.Observable

@Dao
interface CityWeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCityWeather(cityweather: CityWeatherEntity): Long

    @Query("select * from cityweather")
    fun getCityWeathers(): Observable<List<CityWeatherEntity>>

    @Query("select * from cityweather")
    fun getCityWeatherstest(): List<CityWeatherEntity>

    @Query("delete from cityweather where cityId=:id")
    fun removeCityWeather(id: String)

    @Query("delete from cityweather")
    fun removeAllCityWeathers()
}