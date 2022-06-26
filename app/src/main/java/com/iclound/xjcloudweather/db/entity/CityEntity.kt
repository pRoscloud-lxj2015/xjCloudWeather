package com.iclound.xjcloudweather.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName="city")
class CityEntity {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var cityLocation: String = ""

    var cityName: String = ""

    var isLocation: Boolean = false

    constructor()

    @Ignore
    constructor(local: String, name: String, isLocal: Boolean = false) : this() {
        cityLocation = local
        cityName = name
        isLocation = isLocal
    }

}