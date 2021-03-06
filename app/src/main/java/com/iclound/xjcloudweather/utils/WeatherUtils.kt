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
                result = "??????"
            }
            "Blue" -> {
                result = "??????"
            }
            "Green" -> {
                result = "??????"
            }
            "Yellow" -> {
                result = "??????"
            }
            "Orange" -> {
                result = "??????"
            }
            "Red" -> {
                result = "??????"
            }
            "Black" -> {
                result = "??????"
            }
            else -> result = "??????"
        }
        return result
    }

    @JvmStatic
    fun convert(code: Int): String {
        var result = ""
        when (code) {
            100, 150 ->
                result = "qintian"  // ???
            101, 102, 103, 153, 151 ->
                result = "duoyun"  // ??????
            104, 154 ->
                result = "yintian"  // ??????
            300, 301, 306, 313, 315, 350, 399 ->
                result = "yutian"  // ???
            305, 309, 314 ->
                result = "yutian"  // ??????
            307, 308, 310, 311, 312, 316, 317, 318, 351 ->
                result = "yutian"  // ??????
            302, 303, 304 ->
                result = "yintian"  // ??????
            401, 402, 403, 409, 410 ->
                result = "yutian"  // ??????
            in 400..457 ->
                result = "yutian"  // ???
            500, 501, 509, 510, 514, 515 ->
                result = "duoyun"  // ???
            502, 511, 512, 513 ->
                result = "duoyun"  // ???
            in 503..508 ->
                result = "shacheng"  // ??????
            else -> result = "qintian"
        }
        return result
    }

    @JvmStatic
    fun whetherRains(code: Int): String {
        var result = ""
        when (code) {
            300, 301,302, 303,304,308,309,350,351,399 ->  //???
                result = "???"
            305,313  ->//??????
                result = "??????"
            306,314  ->//??????
                result = "??????"
            307,315  ->//??????
                result = "??????"
            310,311,316,317,  ->//??????
                result = "??????"
            312,318, ->//????????????
                result = "????????????"
            404, 405,456,457 ->//?????????
                result = "?????????"
            400,406,407,499 ->//??????
                result = "??????"
            401,408   ->//??????
                result = "??????"
            402,409   ->//??????
                result = "??????"
            403, 410   ->//??????
                result = "??????"
        }
        return result
//            100, 150, 101, 102, 103, 153, 151, 104, 154,
//            350, 399,500, 501, 509,
//            510, 514, 515, 502, 511, 512, 513 ->
//                result = ""
//            300, 301, 305, 306, 313, 315, 350, 399, 305, 309, 314 ->
//                result = "???"  // ??????
//            307, 308, 310, 311, 312, 316, 317, 318, 351 ->
//                result = "??????"  // ??????
//            302, 303, 304 ->
//                result = "??????"  // ??????
//            401, 402, 403, 409, 410 ->
//                result = "??????"  // ??????
//            in 400..457 ->
//                result = "???"  // ???
//            in 503..508 ->
//                result = ""  // ??????
//            else -> result = ""
    }

    @JvmStatic
    fun whetherRainsResId(code: Int): Int {
        var result = R.drawable.rain_fall_normal
        when (code) {
            300, 301, 302, 303, 304, 308, 309, 350, 351, 399 ->  //???
                result =R.drawable.rain_fall_normal //"???"
            305, 313 ->//??????
                result = R.drawable.rain_fall_normal//"??????"
            306, 314 ->//??????
                result = R.drawable.rain_fall_small_rain// "??????"
            307, 315 ->//??????
                result = R.drawable.rain_fall_middle_rain //"??????"
            310, 311, 316, 317, ->//??????
                result = R.drawable.rain_fall_heavy_rain //"??????"
            312, 318, ->//????????????
                result = R.drawable.rain_fall_heavy_rain //"????????????"
            404, 405, 456, 457 ->//?????????
                result = R.drawable.rain_fall_snow //"?????????"
            400, 406, 407, 499 ->//??????
                result = R.drawable.rain_fall_snow //"??????"
            401, 408 ->//??????
                result = R.drawable.rain_fall_snow //"??????"
            402, 409 ->//??????
                result = R.drawable.rain_fall_snow //"??????"
            403, 410 ->//??????
                result = R.drawable.rain_fall_snow //"??????"
        }
        return result
    }

    @JvmStatic
    fun convertWind(dir: String): Int{
        var result = 0
        when(dir){
            "??????" ->{ result = R.drawable.wind_east}
            "??????" ->{ result = R.drawable.wind_north}
            "?????????" ->{ result = R.drawable.wind_northeast}
            "?????????" ->{ result = R.drawable.wind_northwest}
            "??????" ->{ result = R.drawable.wind_south}
            "?????????" ->{ result = R.drawable.wind_southeast}
            "?????????" ->{ result = R.drawable.wind_southwest}
            "??????" ->{ result = R.drawable.wind_west}
        }
        return result
    }
}