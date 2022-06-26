package com.iclound.xjcloudweather.app

import android.app.Application
import android.content.Context
import android.util.Log
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        val TAG = "祥云天气"
        var context: Context by Delegates.notNull()
            private set

        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        initTheme()
        initConfig()
    }

    private fun initTheme() {
        //TODO("Not yet implemented")
    }

    private fun initConfig() {
       val rootDir = MMKV.initialize(this)
        Log.d(TAG, "App: rootDir:" + rootDir)
    }
}
