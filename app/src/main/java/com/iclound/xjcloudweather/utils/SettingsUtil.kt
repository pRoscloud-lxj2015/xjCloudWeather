package com.iclound.xjcloudweather.utils

import android.graphics.Color
import android.preference.PreferenceManager
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.app.App

object SettingsUtil {
    private val setting = PreferenceManager.getDefaultSharedPreferences(App.context)

    fun getColor(): Int {
        val defaultColor = App.context.resources.getColor(R.color.TRANSTION)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else color
    }

    /**
     * 获取是否开启导航栏上色
     */
    fun getNavBar(): Boolean {
        return setting.getBoolean("nav_bar", false)
    }
}