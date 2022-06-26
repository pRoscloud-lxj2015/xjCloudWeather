package com.iclound.xjcloudweather.db.dao

import androidx.room.*
import com.iclound.xjcloudweather.db.entity.CacheEntity
import io.reactivex.rxjava3.core.Observable

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCache(cache: CacheEntity): Long

    @Query("select * from cache where `key`=:key")
    fun getCache(key: String): CacheEntity?

    @Query("select * from cache where `key`=:key")
    fun getCachetest(key: String): Observable<CacheEntity>?

    @Query("select count(*) from cache where `key`=:key")
    fun getCacheEmpty(key: String): Observable<Boolean>

    @Delete
    fun deleteCache(cache: CacheEntity): Int
}