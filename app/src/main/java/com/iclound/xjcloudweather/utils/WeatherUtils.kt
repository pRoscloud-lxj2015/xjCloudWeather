package com.iclound.xjcloudweather.utils

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import com.iclound.xjcloudweather.R

object WeatherUtils {

    fun getWarningRes(context: Context, severityColor: String): Pair<Drawable, Int> {
        val result: Pair<Drawable, Int>
        val res = context.resources
        when (severityColor) {
            "White" -> {
                result = res.getDrawable(
                    R.drawable.shape_white_alarm,
                    null
                ) to res.getColor(R.color.white, null)
            }
            "Blue" -> {
                result = res.getDrawable(
                    R.drawable.shape_blue_alarm,
                    null
                ) to res.getColor(R.color.white, null)
            }
            "Green" -> {
                result = res.getDrawable(
                    R.drawable.shape_green_alarm,
                    null
                ) to res.getColor(R.color.white, null)
            }
            "Yellow" -> {
                result = res.getDrawable(
                    R.drawable.shape_yellow_alarm,
                    null
                ) to res.getColor(R.color.white, null)
            }
            "Orange" -> {
                result = res.getDrawable(
                    R.drawable.shape_orange_alarm,
                    null
                ) to res.getColor(R.color.white, null)
            }
            "Red" -> {
                result = res.getDrawable(R.drawable.shape_red_alarm, null) to res.getColor(
                    R.color.white,
                    null
                )
            }
            else -> {
                result = res.getDrawable(
                    R.drawable.shape_black_alarm,
                    null
                ) to res.getColor(R.color.white, null)
            }
        }
        return result
    }

    fun getWarningResId(context: Context, severityColor: String): Int{
        val result:Int
        when (severityColor) {
            "White" -> {
                result = R.drawable.shape_white_alarm
            }
            "Blue" -> {
                result = R.drawable.shape_blue_alarm
            }
            "Green" -> {
                result = R.drawable.shape_green_alarm
            }
            "Yellow" -> {
                result = R.drawable.shape_yellow_alarm
            }
            "Orange" -> {
                result = R.drawable.shape_orange_alarm
            }
            "Red" -> {
                result = R.drawable.shape_red_alarm
            }
            else -> {
                result = R.drawable.shape_black_alarm

            }
        }
        return result
    }

    fun getWarningLevelStr(color: String): String {
        val result: String
        when (color) {
            "White" -> {
                result = "白色"
            }
            "Blue" -> {
                result = "蓝色"
            }
            "Green" -> {
                result = "绿色"
            }
            "Yellow" -> {
                result = "黄色"
            }
            "Orange" -> {
                result = "橙色"
            }
            "Red" -> {
                result = "红色"
            }
            "Black" -> {
                result = "黑色"
            }
            else -> result = "白色"
        }
        return result
    }

    @JvmStatic
    fun convert(code: Int): String {
        var result = ""
        when (code) {
            100, 150 ->
                result = "qintian"  // 晴
            101, 102, 103, 153, 151 ->
                result = "duoyun"  // 多云
            104, 154 ->
                result = "yintian"  // 阴天
            300, 301, 306, 313, 315, 350, 399 ->
                result = "yutian"  // 雨
            305, 309, 314 ->
                result = "yutian"  // 小雨
            307, 308, 310, 311, 312, 316, 317, 318, 351 ->
                result = "yutian"  // 大雨
            302, 303, 304 ->
                result = "yintian"  // 雷雨
            401, 402, 403, 409, 410 ->
                result = "yutian"  // 大雪
            in 400..457 ->
                result = "yutian"  // 雪
            500, 501, 509, 510, 514, 515 ->
                result = "duoyun"  // 雾
            502, 511, 512, 513 ->
                result = "duoyun"  // 霾
            in 503..508 ->
                result = "shacheng"  // 沙尘
            else -> result = "qintian"
        }
        return result
    }

    @JvmStatic
    fun whetherRains(code: Int): String {
        var result = ""
        when (code) {
            300, 301,302, 303,304,308,309,350,351,399 ->  //雨
                result = "雨"
            305,313  ->//小雨
                result = "小雨"
            306,314  ->//中雨
                result = "中雨"
            307,315  ->//大雨
                result = "大雨"
            310,311,316,317,  ->//暴雨
                result = "暴雨"
            312,318, ->//特大暴雨
                result = "特大暴雨"
            404, 405,456,457 ->//雨夹雪
                result = "雨夹雪"
            400,406,407,499 ->//小雪
                result = "小雪"
            401,408   ->//中雪
                result = "中雪"
            402,409   ->//大雪
                result = "大雪"
            403, 410   ->//暴雪
                result = "暴雪"
        }
        return result
//            100, 150, 101, 102, 103, 153, 151, 104, 154,
//            350, 399,500, 501, 509,
//            510, 514, 515, 502, 511, 512, 513 ->
//                result = ""
//            300, 301, 305, 306, 313, 315, 350, 399, 305, 309, 314 ->
//                result = "雨"  // 小雨
//            307, 308, 310, 311, 312, 316, 317, 318, 351 ->
//                result = "大雨"  // 大雨
//            302, 303, 304 ->
//                result = "雷雨"  // 雷雨
//            401, 402, 403, 409, 410 ->
//                result = "大雪"  // 大雪
//            in 400..457 ->
//                result = "雪"  // 雪
//            in 503..508 ->
//                result = ""  // 沙尘
//            else -> result = ""
    }

    @JvmStatic
    fun whetherRainsResId(code: Int): Int {
        var result = R.drawable.rain_fall_normal
        when (code) {
            300, 301, 302, 303, 304, 308, 309, 350, 351, 399 ->  //雨
                result =R.drawable.rain_fall_normal //"雨"
            305, 313 ->//小雨
                result = R.drawable.rain_fall_normal//"小雨"
            306, 314 ->//中雨
                result = R.drawable.rain_fall_small_rain// "中雨"
            307, 315 ->//大雨
                result = R.drawable.rain_fall_middle_rain //"大雨"
            310, 311, 316, 317, ->//暴雨
                result = R.drawable.rain_fall_heavy_rain //"暴雨"
            312, 318, ->//特大暴雨
                result = R.drawable.rain_fall_heavy_rain //"特大暴雨"
            404, 405, 456, 457 ->//雨夹雪
                result = R.drawable.rain_fall_snow //"雨夹雪"
            400, 406, 407, 499 ->//小雪
                result = R.drawable.rain_fall_snow //"小雪"
            401, 408 ->//中雪
                result = R.drawable.rain_fall_snow //"中雪"
            402, 409 ->//大雪
                result = R.drawable.rain_fall_snow //"大雪"
            403, 410 ->//暴雪
                result = R.drawable.rain_fall_snow //"暴雪"
        }
        return result
    }

    @JvmStatic
    fun convertWind(dir: String): Int{
        var result = 0
        when(dir){
            "东风" ->{ result = R.drawable.wind_east}
            "北风" ->{ result = R.drawable.wind_north}
            "东北风" ->{ result = R.drawable.wind_northeast}
            "西北风" ->{ result = R.drawable.wind_northwest}
            "南风" ->{ result = R.drawable.wind_south}
            "东南风" ->{ result = R.drawable.wind_southeast}
            "西南风" ->{ result = R.drawable.wind_southwest}
            "西风" ->{ result = R.drawable.wind_west}
        }
        return result
    }
}