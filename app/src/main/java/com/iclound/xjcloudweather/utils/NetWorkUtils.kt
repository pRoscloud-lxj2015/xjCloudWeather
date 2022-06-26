package com.iclound.xjcloudweather.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission
import com.iclound.xjcloudweather.app.App

object NetWorkUtils {

    /**
     * 获取活动网络信息
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 判断网络是否链接
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean{
        return isConnected(App.context)
    }

    /**
     * 判断网络是否链接
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isConnected(context: Context): Boolean{
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断wifi是否连接状态
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }
}