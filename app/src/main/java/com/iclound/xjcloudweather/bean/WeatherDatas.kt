package com.iclound.xjcloudweather.base

import java.io.Serializable


/**
 * 公共数据
 */
data class Refer(
    val license: List<String>,
    val sources: List<String>
): Serializable

/**
 * 实时天气数据
 */
data class WeatherDatas(
    val code: String,
    val fxLink: String,
    val now: Now,
    val refer: Refer,
    val updateTime: String
): Serializable

data class Now(
    val cloud: String,
    val dew: String,
    val feelsLike: String,
    val humidity: String,
    val icon: String,
    val obsTime: String,
    val precip: String,
    val pressure: String,
    val temp: String,
    val text: String,
    val vis: String,
    val wind360: String,
    val windDir: String,
    val windScale: String,
    val windSpeed: String
): Serializable

/**
 * 24小时数据
 */
data class HourlyDatas(
    val code: String,
    val fxLink: String,
    val hourly: MutableList<Hourly>,
    val refer: Refer,
    val updateTime: String
): Serializable

data class Hourly(
    val cloud: String,
    val dew: String,
    val fxTime: String,
    val humidity: String,
    val icon: String,
    val pop: String,
    val precip: String,
    val pressure: String,
    val temp: String,
    val text: String,
    val wind360: String,
    val windDir: String,
    val windScale: String,
    val windSpeed: String
): Serializable

/**
 * 每天数据
 */

data class DaylyDatas(
    val code: String,
    val daily: MutableList<Daily>,
    val fxLink: String,
    val refer: Refer,
    val updateTime: String
): Serializable

data class Daily(
    val cloud: String,
    val fxDate: String,
    val humidity: String,
    val iconDay: String,
    val iconNight: String,
    val moonPhase: String,
    val moonPhaseIcon: String,
    val moonrise: String,
    val moonset: String,
    val precip: String,
    val pressure: String,
    val sunrise: String,
    val sunset: String,
    val tempMax: String,
    val tempMin: String,
    val textDay: String,
    val textNight: String,
    val uvIndex: String,
    val vis: String,
    val wind360Day: String,
    val wind360Night: String,
    val windDirDay: String,
    val windDirNight: String,
    val windScaleDay: String,
    val windScaleNight: String,
    val windSpeedDay: String,
    val windSpeedNight: String
): Serializable

/**
 * 实时天气质量
 */

data class AirDailyDatas(
    val code: String,
    val fxLink: String,
    val now: Air,
    val refer: Refer,
    val updateTime: String
): Serializable

data class Air(
    val aqi: String,
    val category: String,
    val co: String,
    val level: String,
    val no2: String,
    val o3: String,
    val pm10: String,
    val pm2p5: String,
    val primary: String,
    val pubTime: String,
    val so2: String
): Serializable

/**
 * 定位天气数据
 */
data class LocationDatas(
    val code: String,
    val poi: MutableList<Poi>,
    val refer: Refer
)

data class Poi(
    val adm1: String,
    val adm2: String,
    val country: String,
    val fxLink: String,
    val id: String,
    val isDst: String,
    val lat: String,
    val lon: String,
    val name: String,
    val rank: String,
    val type: String,
    val tz: String,
    val utcOffset: String
)

/**
 * 定位数据
 */
data class Location(
    val cityName: String, //城市
    val cityId: String, //城市id
    val lat: String, //纬度
    val lon: String, //经度
    val dis: String, //城区
    val street: String  //街道信息
)

/**
 * 天气预警
 */
data class WarningNowDatas(
    val code: String,
    val fxLink: String,
    val refer: Refer,
    val updateTime: String,
    val warning: List<Warning>
): Serializable

data class Warning(
    val endTime: String,
    val id: String,
    val level: String,
    val pubTime: String,
    val related: String,
    val sender: String,
    val severity: String,
    val severityColor: String,
    val startTime: String,
    val status: String,
    val text: String,
    val title: String,
    val type: String,
    val typeName: String
): Serializable

/**
 * 搜索城市
 */
data class SearchCityDatas(
    val code: String,
    val location: List<Locations>,
    val refer: Refer
)

data class Locations(
    val adm1: String,
    val adm2: String,
    val country: String,
    val fxLink: String,
    val id: String,
    val isDst: String,
    val lat: String,
    val lon: String,
    val name: String, // 地区/城市名称
    val rank: String,
    val type: String,
    val tz: String,
    val utcOffset: String
)


/**
 * 热门城市
 */
data class TopCityDatas(
    val code: String,
    val refer: Refer,
    val topCityList: MutableList<TopCity>
)

data class TopCity(
    val adm1: String,   //地区/城市的上级行政区划名称
    val adm2: String,   //地区/城市所属一级行政区域
    val country: String,
    val fxLink: String,
    val id: String,   //地区/城市ID
    val isDst: String,
    val lat: String,  // 地区/城市纬度
    val lon: String,  //地区/城市经度
    val name: String, // 地区/城市名称
    val rank: String,
    val type: String,
    val tz: String,
    val utcOffset: String
)

data class CurTopCity(
    val cityId: String,
    val name: String,
    val islocal: Boolean = false,
    val isAdd: Boolean = false
)

data class CityBean(
    var cityName: String = "",
    var cityId: String = "",
    var cnty: String = "",
    val location: String = "",
    val parentCity: String = "",
    var adminArea: String = "",
    val isFavor: Boolean = false,
    val searchCityWeather: MutableList<SearchTargetCity>
)

data class SearchTargetCity(
    val fxDate: String = "",
    val tempMax: String = "",
    val tempMin: String = "",
    val iconDay: String="",
    val iconNight: String=""
)


