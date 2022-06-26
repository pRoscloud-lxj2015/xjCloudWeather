package com.iclound.xjcloudweather.db

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iclound.xjcloudweather.db.dao.CacheDao
import com.iclound.xjcloudweather.db.dao.CityDao
import com.iclound.xjcloudweather.db.dao.CityWeatherDao
import com.iclound.xjcloudweather.db.entity.CacheEntity
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity

@Database(
    entities=[CacheEntity::class, CityEntity::class, CityWeatherEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class AppDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun cityDao(): CityDao

    abstract fun cityWeatherDao(): CityWeatherDao

    companion object{
        val DATABASE_NAME= "xcloud-weather.db"

        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase{
            return instance?: synchronized(this){
                instance
                    ?: buildDatabase(context).also{ instance = it}
            }
        }

        private fun buildDatabase(context: Context): AppDataBase{
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                    .allowMainThreadQueries()
                 .addCallback(object: RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.e("db","db: onCreate")
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Log.e("db","db: onOpen")
                    }
                }).build()
        }
    }
}