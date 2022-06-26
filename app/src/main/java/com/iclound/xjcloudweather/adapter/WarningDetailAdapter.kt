package com.iclound.xjcloudweather.adapter

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.base.Warning
import com.iclound.xjcloudweather.utils.DataUtils
import com.iclound.xjcloudweather.utils.IconUtils
import com.iclound.xjcloudweather.utils.WeatherUtils
import kotlinx.android.synthetic.main.warning_infor_layout.view.*
import java.lang.Math.abs

class WarningDetailAdapter: BaseQuickAdapter<Warning, BaseViewHolder>(R.layout.item_warning_desc) {

    override fun convert(holder: BaseViewHolder, item: Warning) {
        val level: String = WeatherUtils.getWarningLevelStr(item.severityColor)
        val tip = item.typeName + level + "预警"
        holder.setText(R.id.warning_title_text, tip)
        holder.setImageResource(R.id.warning_desc_img, IconUtils.getDayIcon(context, "icon_" + item.type))
        holder.setBackgroundResource(R.id.warning_desc_img,  WeatherUtils.getWarningResId(context, item.severityColor))
        val currDay = DataUtils.getNowDay()
        val currHour = DataUtils.getNowHour()
        val currMin = DataUtils.getNowMin()
        val pubDay = DataUtils.getNowDay(item.pubTime).toInt()
        val pubHour = DataUtils.getNowHourly(item.pubTime)
        val pubMin = DataUtils.getNowMinute(item.pubTime)
        Log.d("WarningDetail" ,"Warning+ pubHour:" + pubDay + " pubHour:" + pubHour + " pubMin:" + pubMin)
        if(currMin - pubMin  <= 20 && currHour == pubHour && currDay == pubDay){ //小于20风，则刚刚更新
            holder.setText(R.id.warning_desc_time, "刚刚更新")
        }else if(currMin - pubMin > 20 && currHour == pubHour && currDay == pubDay){
            holder.setText(R.id.warning_desc_time, "半小时前更新")
        }else if(currHour - pubHour >= 1 && currDay == pubDay){
            holder.setText(R.id.warning_desc_time, "${currHour - pubHour}小时前更新")
        }else if(abs(currHour - pubHour) >= 0 && currDay != pubDay){
            if(((currHour - pubHour) + 24) > 24)
                holder.setText(R.id.warning_desc_time, "1天前更新")
            else
                holder.setText(R.id.warning_desc_time, "${(currHour - pubHour) + 24}小时前更新")
        }
        holder.setText(R.id.warning_desc_text, item.text)
    }
}

