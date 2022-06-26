package com.iclound.xjcloudweather.adapter

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.Hourly
import com.iclound.xjcloudweather.utils.DataUtils
import com.iclound.xjcloudweather.utils.IconUtils
import com.iclound.xjcloudweather.utils.WeatherUtils

/**
 * 24小时天气数据
 */
class HourlyAdapter : BaseQuickAdapter<Hourly, BaseViewHolder>(R.layout.item_forecast15),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Hourly) {
        holder.setText(R.id.tv_hours, DataUtils.getNowHourly(item.fxTime).toString()+":00")
        holder.setText(R.id.tw_temp, item.temp+"°")
        holder.setImageResource(R.id.tw_weather_img, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + item.icon))
        holder.setImageResource(R.id.tw_wind_desc, WeatherUtils.convertWind(item.windDir))
        holder.getView<ImageView>(R.id.tw_weather_img).drawable.mutate()
        holder.getView<ImageView>(R.id.tw_weather_img).drawable.setTint(ContextCompat.getColor(App.context, R.color.white))
        val windScale = item.windScale
        val scales: List<String> = windScale.split("-")
        if(scales.size > 0)
            holder.setText(R.id.tw_wind_rank, scales[scales.size - 1] + "级")
        else
            holder.setText(R.id.tw_wind_rank, scales[scales.size] + "级")

    }
}