package com.iclound.xjcloudweather.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object LunarUtils {
    private var year = 0
    private var month = 0
    private var day = 0
    private var leap = false

    var chineseDateFormat = SimpleDateFormat("yyyy年MM月dd日")
    val chineseNumber = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")


    val lunarInfo = longArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
    )

    fun lunarOfDays(): String {
        val cal = Calendar.getInstance()
        var monCyl: Int
        var leapMonth = 0
        var baseDate: Date? = null
        try {
            baseDate = chineseDateFormat.parse("1900年1月31日")
        } catch (e: ParseException) {
            e.printStackTrace() //To change body of catch statement use Options | File Templates.
        }

        //求出和1900年1月31日相差的天数
        var offset = ((cal.time.time - baseDate!!.time) / 86400000L).toInt()
        monCyl = 14

        //用offset减去每农历年的天数
        // 计算当天是农历第几天
        //i最终结果是农历的年份
        //offset是当年的第几天
        var iYear: Int
        var daysOfYear = 0
        iYear = 1900
        while (iYear < 2050 && offset > 0) {
            daysOfYear = yearDays(iYear)
            offset -= daysOfYear
            monCyl += 12
            iYear++
        }
        if (offset < 0) {
            offset += daysOfYear
            iYear--
            monCyl -= 12
        }
        //农历年份
        year = iYear
        leapMonth = leapMonth(iYear) //闰哪个月,1-12
        leap = false

        //用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        var iMonth: Int
        var daysOfMonth = 0
        iMonth = 1
        while (iMonth < 13 && offset > 0) {

            //闰月
            if (leapMonth > 0 && iMonth == leapMonth + 1 && !leap) {
                --iMonth
                leap = true
                daysOfMonth = leapDays(year)
            } else {
                daysOfMonth = monthDays(year, iMonth)
            }
            offset -= daysOfMonth
            //解除闰月
            if (leap && iMonth == leapMonth + 1) {
                leap = false
            }
            if (!leap) {
                monCyl++
            }
            iMonth++
        }
        //offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false
            } else {
                leap = true
                --iMonth
                --monCyl
            }
        }
        //offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth
            --iMonth
            --monCyl
        }
        month = iMonth
        day = offset + 1
        return (if (leap) "闰" else "") + chineseNumber.get(month - 1) + "月" + getChinaDayString(day)
    }

    //====== 传回农历 y年的总天数
    private fun yearDays(y: Int): Int {
        var i: Int
        var sum = 348
        i = 0x8000
        while (i > 0x8) {
            if (lunarInfo.get(y - 1900) and i.toLong() != 0L) {
                sum += 1
            }
            i = i shr 1
        }
        return sum + leapDays(y)
    }

    //====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
    private fun leapMonth(y: Int): Int {
        return (lunarInfo.get(y - 1900) and 0xf).toInt()
    }

    //====== 传回农历 y年m月的总天数
    private fun monthDays(y: Int, m: Int): Int {
        return if (lunarInfo.get(y - 1900) and ((0x10000 shr m).toLong()) == 0L) {
            29
        } else {
            30
        }
    }

    //====== 传回农历 y年闰月的天数
    private fun leapDays(y: Int): Int {
        return if (leapMonth(y) != 0) {
            if (lunarInfo.get(y - 1900) and 0x10000 != 0L) {
                30
            } else {
                29
            }
        } else {
            0
        }
    }

    fun getChinaDayString(day: Int): String? {
        val chineseTen = arrayOf("初", "十", "廿", "三")
        val n = if (day % 10 == 0) 9 else day % 10 - 1
        if (day > 30) {
            return ""
        }
        return if (day == 10) {
            "初十"
        } else {
            chineseTen[day / 10] + chineseNumber.get(n)
        }
    }


}