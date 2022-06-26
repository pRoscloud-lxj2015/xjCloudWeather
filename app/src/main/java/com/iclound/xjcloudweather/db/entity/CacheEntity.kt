package com.iclound.xjcloudweather.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName= "cache")
class CacheEntity {

    @PrimaryKey(autoGenerate = false)
    @NotNull
    var key: String =""

    //缓存数据为二进制
    var data: ByteArray? = null

    var dead_line: Long = 0
}