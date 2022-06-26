package com.iclound.xjcloudweather.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DataUtils {

    val weeks = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")

    @JvmStatic
    fun getNowHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    @JvmStatic
    fun getNowYear(): Int{
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    @JvmStatic
    fun getNowMonth(): Int{
        return Calendar.getInstance().get(Calendar.MONTH)
    }

    @JvmStatic
    fun getNowDay(): Int{
        return Calendar.getInstance().get(Calendar.DATE)
    }

    @JvmStatic
    fun getNowMin(): Int{
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    @JvmStatic
    fun getNowDay(date: String): String{
        val date = formatter.parse(date) as Date
        return formatter.calendar[Calendar.DATE].toString()
    }

    @JvmStatic
    fun getNowHourly(date: String): Int{
        val date = formatter.parse(date) as Date
        return formatter.calendar[Calendar.HOUR_OF_DAY]
    }

    @JvmStatic
    fun getNowMinute(date: String): Int{
        val date = formatter.parse(date) as Date
        return formatter.calendar[Calendar.MINUTE]
    }

    fun getNoMoreThanTwoDigits(number: Double?): String {
        val format = DecimalFormat("0.##")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }

    fun getWeekDay(position: Int, fxDate: String ): String {
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

}