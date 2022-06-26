package com.iclound.xjcloudweather.adapter

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.BaseMultiAdapter
import com.iclound.xjcloudweather.base.SearchTargetCity
import com.iclound.xjcloudweather.utils.IconUtils
import java.util.*

class SearchCityWeatherAdapter(layoutId: Int): BaseMultiAdapter<SearchTargetCity>(layoutId) {

    override fun getItemLayoutId(): Int = R.layout.item_search_forecast5

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun convert(holder: BaseViewHolder, item: SearchTargetCity) {
        holder.setText(R.id.sw_day, getWeekDay(holder.layoutPosition, item.fxDate))
        var icon: Int = 0
        when(holder.layoutPosition) {
            0 -> {
                icon = if (IconUtils.isDay()) {
                    IconUtils.getDayIcon(App.context, "icon_" + item.iconDay)
                } else {
                    IconUtils.getDayIcon(App.context, "icon_" + item.iconNight)
                }
            }
            1 -> {
                icon = IconUtils.getDayIcon(App.context, "icon_" + item.iconDay)
            }else -> {
                icon = IconUtils.getDayIcon(App.context, "icon_" + item.iconDay)
                //holder.setImageResource(R.id.sw_weather_img, IconUtils.getDayIcon(App.context, "icon_" + item.iconDay))
            }
        }
        holder.setImageResource(R.id.sw_weather_img, icon)
        holder.getView<ImageView>(R.id.sw_weather_img).drawable.mutate()
        holder.getView<ImageView>(R.id.sw_weather_img).drawable.setTint(ContextCompat.getColor(App.context, R.color.search_city_icon_bg))
        holder.setText(R.id.sw_temp_max, item.tempMax + "°")
        holder.setText(R.id.sw_temp_min, item.tempMin + "°")
    }

    val weeks = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    private fun getWeekDay(position: Int, fxDate: String ): String {
        return if (position == 0) {
            "今天"
        } else if(position == 1){
            "明天"
        } else{
            val calendar = Calendar.getInstance()
            val dateArray = fxDate.split("-")
            calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
            var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
            if (w < 0) {
                w = 0
            }
            weeks[w]
        }
    }


}