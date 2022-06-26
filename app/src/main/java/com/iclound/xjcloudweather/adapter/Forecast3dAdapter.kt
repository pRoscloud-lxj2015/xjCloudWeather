package com.iclound.xjcloudweather.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.Daily
import com.iclound.xjcloudweather.utils.DataUtils
import com.iclound.xjcloudweather.utils.IconUtils

/**
 * 3天数据Adapter
 */
class Forecast3dAdapter : BaseQuickAdapter<Daily, BaseViewHolder>(R.layout.layout_forecast3d_weather),
    LoadMoreModule {

    @SuppressLint("ResourceAsColor")
    override fun convert(holder: BaseViewHolder, daily: Daily) {
        when(holder.layoutPosition){
            0 -> {
                holder.setText(R.id.tw_day, "今天")
                if(IconUtils.isDay()){
                    holder.setImageResource(R.id.tw_weather, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + daily.iconDay))
                }else{
                    holder.setImageResource(R.id.tw_weather, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + daily.iconNight))
                }

            }
            1 -> {
                holder.setImageResource(R.id.tw_weather, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + daily.iconDay))

                holder.setText(R.id.tw_day, "明天")
            }
            else -> {
                holder.setImageResource(R.id.tw_weather, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + daily.iconDay))
                holder.setText(R.id.tw_day, DataUtils.getWeekDay(holder.layoutPosition, daily.fxDate))
               // holder.setText(R.id.tw_day, "后天")
            }
        }
        holder.getView<ImageView>(R.id.tw_weather)?.drawable?.setTint(ContextCompat.getColor(App.context, R.color.white))
        var desc = daily.textDay
        if(daily.textDay != daily.textNight){
            desc += "转" + daily.textNight
        }
        //holder.setText(R.id.tw_dian, "&#183;".toString())
        holder.setText(R.id.tw_text, desc)
        holder.setText(R.id.tw_temp, "${daily.tempMax}° / ${daily.tempMin}°")

    }

}