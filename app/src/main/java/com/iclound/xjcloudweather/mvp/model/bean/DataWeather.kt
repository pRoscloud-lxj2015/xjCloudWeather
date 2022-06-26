package com.iclound.xjcloudweather.mvp.model.bean

import com.iclound.xjcloudweather.bean.BaseBean
import com.squareup.moshi.Json

data class DataWeather<T> (
    @Json(name="data") val data: T
) : BaseBean()

/**
 * 实时天气
 */
data class RealDataWeather(
    @Json(name="code") val code: String,  //API状态码，具体含义请参考状态码
    @Json(name="updateTime") val updateTime: String, //当前API的最近更新时间
    @Json(name="fxLink") val fxLink: String, //当前数据的响应式页面，便于嵌入网站或应用
    @Json(name = "now") val now:  MutableList<Now>,
    @Json(name = "refer") val refer:  MutableList<Refer>
)

data class Now(
    @Json(name = "obsTime") val obsTime:String, //数据观测时间
    @Json(name = "temp") val temp: String, //温度，默认单位：摄氏度
    @Json(name = "feelsLike") val feelsLike: String,//体感温度，默认单位：摄氏度
    @Json(name = "icon") val icon: String,//天气状况和图标的代码，图标可通过天气状况和图标下载
    @Json(name = "text") val text: String,//天气状况的文字描述，包括阴晴雨雪等天气状态的描述
    @Json(name = "wind360") val wind360: String, //风向360角度
    @Json(name = "windDir") val windDir: String, //风向
    @Json(name = "windScale")  val windScale: String, //风力等级
    @Json(name = "windSpeed")  val windSpeed: String, //风速，公里/小时
    @Json(name = "humidity") val  humidity: String, //相对湿度，百分比数值
    @Json(name = "precip") val precip: String, //当前小时累计降水量，默认单位：毫米
    @Json(name = "pressure") val pressure: String, //大气压强，默认单位：百帕
    @Json(name = "vis") val vis: String, //能见度，默认单位：公里
    @Json(name = "cloud") var cloud: String, //云量，百分比数值。可能为空
    @Json(name = "dew") var dew: String //露点温度。可能为空
)

data class Refer(
    @Json(name = "sources")   var sources: ArrayList<Sources>, //原始数据来源，或数据源说明，可能为空
    @Json(name = "license")   var license: ArrayList<License> //数据许可或版权声明，可能为空
)

data class Sources(
    @Json(name = "QWeather") val qweather: String,
    @Json(name = "NMC") val nmc: String,
    @Json(name = "ECMWF") val ecmwf: String,
)

data class License(
    @Json(name = "commercial license") val license: String,
)
