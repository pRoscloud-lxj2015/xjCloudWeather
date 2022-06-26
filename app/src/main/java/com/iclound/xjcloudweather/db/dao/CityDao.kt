package com.iclound.xjcloudweather.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iclound.xjcloudweather.db.entity.CityEntity
import io.reactivex.rxjava3.core.Observable

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCity(city: CityEntity): Long

    @Query("select * from city")
    fun getCities(): List<CityEntity>

    @Query("select * from city")
    fun getObsCities(): Observable<List<CityEntity>>

    @Query("delete from city where cityLocation=:id")
    fun removeCity(id: String)

    @Query("delete from city")
    fun removeAllCity()

}