package com.iclound.xjcloudweather.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.Daily
import com.iclound.xjcloudweather.utils.IconUtils
import com.iclound.xjcloudweather.utils.WeatherUtils
import com.iclound.xjcloudweather.widgets.TempChartView
import java.util.*

class Forecast7dAdapter(var pos: Int = 0) : BaseQuickAdapter<Daily, BaseViewHolder>(R.layout.layout_forecast7d_weather) {
    private var mMin = 0
    private var mMax = 0

    override fun convert(holder: BaseViewHolder, item: Daily) {
        Log.d("", " icon:" + item.iconDay + item.iconNight)
        if(holder.layoutPosition == pos)
            holder.itemView.setBackgroundResource(R.drawable.ic_7d_select_bg)
        else
            holder.itemView.setBackgroundResource(0)
        holder.setText(R.id.td_week, getWeekDay(holder.layoutPosition, item.fxDate))
        holder.setText(R.id.td_date, item.fxDate.removeRange(IntRange(0, 4)))
        holder.setText(R.id.td_day_desc, item.textDay)
        holder.setImageResource(R.id.id_Day, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + item.iconDay))
        holder.setImageResource(R.id.id_night, IconUtils.getDayIcon(App.context.applicationContext, "icon_" + item.iconNight))
        holder.getView<ImageView>(R.id.id_night).drawable.mutate()
        holder.getView<ImageView>(R.id.id_night).drawable?.setTint(ContextCompat.getColor(App.context, R.color.search_city_icon_bg))
        holder.getView<ImageView>(R.id.id_Day).drawable.mutate()
        holder.getView<ImageView>(R.id.id_Day).drawable?.setTint(ContextCompat.getColor(App.context, R.color.search_city_icon_bg))
        holder.setText(R.id.td_night_desc, item.textNight)

        //设置风向导航标
        holder.setImageResource(R.id.td_wind_desc, WeatherUtils.convertWind(item.windDirDay))
        holder.getView<ImageView>(R.id.td_wind_desc).drawable.mutate()
        holder.getView<ImageView>(R.id.td_wind_desc).drawable.setTint(ContextCompat.getColor(App.context, R.color.windscale_img_bg))

        val windScale = item.windScaleDay
        val scales: List<String> = windScale.split("-")
        if(scales.size > 0)
            holder.setText(R.id.td_wind_scale, scales[scales.size - 1] + "级")
        else
            holder.setText(R.id.td_wind_scale, scales[scales.size] + "级")
        //holder.setText(R.id.td_wind_scale, item.windScaleDay + "级")
        val tempChart = holder.getView(R.id.id_tempChart) as TempChartView
        tempChart.setData(
            mMin, mMax,
            if (holder.layoutPosition == 0) null else data[holder.layoutPosition - 1],
            item,
            if (holder.layoutPosition == data.size - 1) null else data[holder.layoutPosition + 1])
        val width = (context.resources.displayMetrics.widthPixels) / 5f
        val params = ViewGroup.LayoutParams(width.toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
        holder.itemView.layoutParams = params
        //        holder.tempChart?.setData(
//            mMin,
//            mMax,
//            if (position == 0) null else datas[position - 1],
//            item,
//            if (position == datas.size - 1) null else datas[position + 1]
//        )
    }

    val weeks = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    private fun getWeekDay(position: Int, fxDate: String ): String {
        return if (position == 0) {
            "今天"
        } else {
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

    fun setRange(min: Int, max: Int) {
        mMin = min
        mMax = max
        //notifyDataSetChanged()
    }
}
//class Forecast7dAdapter(context: Context, data: MutableList<Daily>) : RecyclerView.Adapter<Forecast7dAdapter.ViewHolder>() {
//
//    val weeks = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
//
//    val mTempDay = IntArray(7)
//    val mTempNight = IntArray(7)
//
//    private fun getWeekDay(position: Int, fxDate: String ): String {
//        return if (position == 0) {
//            "今天"
//        } else {
//            val calendar = Calendar.getInstance()
//            val dateArray = fxDate.split("-")
//            calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
//            var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
//            if (w < 0) {
//                w = 0
//            }
//            weeks[w]
//        }
//    }
//    private var mMin = 0
//    private var mMax = 0
//
//    private var datas: MutableList<Daily>
//    private var context: Context? = null
//    init {
//        datas =data
//        this.context = context
//        var count = 0
//        datas.forEach { item ->
//            mTempDay[count] = item.tempMax.toInt()
//            mTempNight[count] = item.tempMin.toInt()
//            count++
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_forecast7d_weather, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = datas[position]
//        holder.week?.setText(getWeekDay(position, item.fxDate))
//        holder.date?.setText(item.fxDate.removeRange(IntRange(0, 4)))
//        holder.day_desc?.setText(item.textDay)
//        holder.night_desc?.setText(item.textNight)
//        holder.wind?.setText(item.windDirDay)
//        holder.wind_scale?.setText(item.windScaleDay + "级")
//        holder.day?.setImageResource(IconUtils.getDayIcon(App.context, "icon_" + item.iconDay))
//        holder.day?.drawable?.setTint(ContextCompat.getColor(App.context, R.color.search_city_icon_bg))
//        holder.night?.setImageResource(IconUtils.getDayIcon(App.context, "icon_" + item.iconNight))
//        holder.night?.drawable?.setTint(ContextCompat.getColor(App.context, R.color.search_city_icon_bg))
//        holder.tempChart?.setData(
//            mMin,
//            mMax,
//            if (position == 0) null else datas[position - 1],
//            item,
//            if (position == datas.size - 1) null else datas[position + 1]
//        )
//        //setRange(item.tempMin.toInt(),
//        //    item.tempMax.toInt())
//        //setEasyTemp(item.tempMax.toInt(), item.tempMin.toInt())
//    }
//
//    fun setRange(min: Int, max: Int) {
//        mMin = min
//        mMax = max
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int {
//        if (datas == null) {
//            return 0;
//        }
//        return datas.size
//    }
//
//    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view
//    ) {
//        var day: ImageView? = null
//        var night: ImageView? = null
//
//        var week: TextView? = null
//        var date: TextView? = null
//        var day_desc: TextView? = null
//        var night_desc: TextView? = null
//        var wind: TextView? = null
//        var tempChart: TempChartView? = null
//        var wind_scale: TextView? = null
//
//        init {
//            week = view?.findViewById(R.id.td_week)
//            date = view?.findViewById(R.id.td_date)
//            day_desc = view?.findViewById(R.id.td_day_desc)
//            night_desc = view?.findViewById(R.id.td_night_desc)
//            wind = view?.findViewById(R.id.td_wind)
//            tempChart = view?.findViewById(R.id.id_tempChart)
//            wind_scale = view?.findViewById(R.id.td_wind_scale)
//            day = view?.findViewById(R.id.id_Day)
//            night = view?.findViewById(R.id.id_night)
//        }
//    }
//
//}

