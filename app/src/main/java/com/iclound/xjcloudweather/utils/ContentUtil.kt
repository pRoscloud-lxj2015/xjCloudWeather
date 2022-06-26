package com.iclound.xjcloudweather.utils

object ContentUtil {
    @JvmField
    var CITY_CHANGE = false

    @JvmField
    var CITY_ID = ""

    @JvmField
    var JUMP_ID = -1

    const val CITY_ADD_COUNT = 10

    const val REFRESH_WEATHER_DATA_TIME =8 * 60 * 1000 // 480000 //8分钟刷新一次即480秒  单位毫秒

    const val ADPATER_DRAG_FLAG = 0x11101112
}