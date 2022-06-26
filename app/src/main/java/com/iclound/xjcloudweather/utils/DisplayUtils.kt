package com.iclound.xjcloudweather.utils

import android.content.Context
import android.content.res.Resources

object DisplayUtils {


    /**
     * dp转px
     */
    fun dp2px(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     */
    fun px2dp(pxVal: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxVal / scale + 0.5f).toInt().toFloat()
    }

    fun sp2px(sp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (sp * scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, sp: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }

}