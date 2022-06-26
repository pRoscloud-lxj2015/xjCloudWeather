package com.iclound.xjcloudweather.base

import com.iclound.xjcloudweather.db.entity.CityWeatherEntity

interface onItemDragSwipListener {
    fun onItemDragEnd(weatherData: List<CityWeatherEntity>)

    //fun onItemSwipeEnd()
}