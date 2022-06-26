package com.iclound.xjcloudweather.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.utils.DataUtils.getNowHour

object IconUtils {

    @JvmStatic
    fun getDayIcon(context: Context, weatherCode: String): Int {
        return App.context.resources.getIdentifier(weatherCode, "drawable",  App.context.packageName)
    }

    @JvmStatic
    fun isDay(): Boolean {
        val now = getNowHour()
        return now in 7..18
    }

    fun getBackg(context: Context, code: Int): Int{
        return if(isDay()) getDayBg(context, code) else getNightBg(context, code)
    }

    /**
     * 获取白天背景
     */
    fun getDayBg(context: Context, code: Int): Int {
        val newCode = WeatherUtils.convert(code)
        return getDrawableRes(context, "bg_" + newCode + "_d", R.drawable.bg_qintian_d)
    }

    fun getNightBg(context: Context, code: Int): Int{
        val newCode = WeatherUtils.convert(code)
        return getDrawableRes(context, "bg_" + newCode + "_n", R.drawable.bg_qintian_n)
    }

    fun getGradienBg(context: Context, code: Int, tar: String): Int{
        return if(isDay()) getDayColor(context, code, tar) else getNightColor(context, code, tar)
    }

    fun getDayColor(context: Context, code: Int, tar: String): Int {
        val newCode = WeatherUtils.convert(code)
        return getColorRes(context, newCode + "_d" + tar)
    }

    fun getNightColor(context: Context, code: Int, tar: String): Int{
        val newCode = WeatherUtils.convert(code)
        return getColorRes(context, newCode + "_n" + tar)
    }

    fun getDrawableRes(context: Context, weather: String, def: Int): Int {
        return getRes(context, "drawable", weather, def)
    }

    fun getColorRes(context: Context, color: String): Int{
        Log.d("getColorRes:" ,  color)
        return getRes(context, "color", color, R.color.qintian_dstart)
    }

    fun setDrawableFillColor(context: Context, color: Int){

    }

    fun getRes(context: Context, type: String?, weather: String, def: Int): Int {
        return try {
            var id = context.resources.getIdentifier(weather, type, context.packageName)
            if (id == 0) {
                id = def
            }
            id
        } catch (e: Exception) {
            Log.d("","获取资源失败：$weather")
            def
        }
    }
}