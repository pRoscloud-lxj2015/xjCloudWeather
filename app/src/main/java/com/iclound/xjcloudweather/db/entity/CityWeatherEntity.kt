package com.iclound.xjcloudweather.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName="cityweather")
class CityWeatherEntity {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var cityName: String = ""

    var cityId: String = ""

    var airNow: String? = ""

    var tempMaxMin: String? = ""

    var tempNow: String? = ""

    var nowIcon: String? = ""

    var isLocation: Boolean = false

    constructor()

    @Ignore
    constructor(name: String, id: String, air: String?, tempmmin: String?, temp: String?, icon: String?,  isLocal: Boolean = false) : this() {
        cityId = id
        cityName = name
        airNow = air
        tempMaxMin = tempmmin
        tempNow = temp
        nowIcon = icon
        isLocation = isLocal
    }
}