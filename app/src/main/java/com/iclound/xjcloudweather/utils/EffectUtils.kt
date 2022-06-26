package com.iclound.xjcloudweather.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.ui.uieffects.EffectCloudDrawable
import me.wsj.lib.specialeffects.*

/**
 * 特效
 */
class EffectUtils {
    companion object {
        fun getEffect(context: Context, code: Int): Drawable? {
            val isDay = DataUtils.getNowHour() in 6 until 18
            ///val type = WeatherUtils.convert(code)
            return when (code) {
                100, 150  -> {
                    if (isDay)
                        EffectSunDrawable(context.resources.getDrawable(R.drawable.sun_icon, null))
                    else
                        EffectMoonNDrawable(context.resources.getDrawable(R.drawable.bg_fine_night_moon, null))
                }
                101, 102, 103, 104, 153, 151 -> {
                    if (isDay) {
                        EffectCloudDrawable(
                            arrayOf(
                                context.resources.getDrawable(R.drawable.cloudy_day_1, null),
                                context.resources.getDrawable(R.drawable.cloudy_day_2, null),
                                context.resources.getDrawable(R.drawable.cloudy_day_3, null),
                                context.resources.getDrawable(R.drawable.cloudy_day_4, null)
                            )
                        )
                    } else
                        EffectCloudDrawable(
                            arrayOf(
                                context.resources.getDrawable(R.drawable.cloudy_day_1, null),
                                context.resources.getDrawable(R.drawable.cloudy_night1, null),
                                context.resources.getDrawable(R.drawable.cloudy_night2, null)
                            )
                        )
                }
                154 -> {
                    EffectCloudDrawable(
                        arrayOf(
                            context.resources.getDrawable(R.drawable.fog_cloud_1, null),
                            context.resources.getDrawable(R.drawable.fog_cloud_2, null)
                        )
                    )
                }
                300, 301, 306, 313, 315, 350, 399 -> {  // 中雨
                    EffectRainDrawable(
                        1,
                        arrayOf(
                            context.resources.getDrawable(R.drawable.raindrop_s, null),
                            context.resources.getDrawable(R.drawable.raindrop_m, null),
                            context.resources.getDrawable(R.drawable.raindrop_l, null),
                            context.resources.getDrawable(R.drawable.raindrop_xl, null)
                        )
                    )
                }
                305, 309, 314 -> {  // 小雨
                    EffectRainDrawable(
                        0,
                        arrayOf(
                            context.resources.getDrawable(R.drawable.raindrop_s, null),
                            context.resources.getDrawable(R.drawable.raindrop_m, null),
                            context.resources.getDrawable(R.drawable.raindrop_l, null),
                            context.resources.getDrawable(R.drawable.raindrop_xl, null)
                        )
                    )
                }
                307, 308, 310, 311, 312, 316, 317, 318, 351 -> {  // 大雨
                    EffectRainDrawable(
                        2,
                        arrayOf(
                            context.resources.getDrawable(R.drawable.raindrop_m, null),
                            context.resources.getDrawable(R.drawable.raindrop_l, null),
                            context.resources.getDrawable(R.drawable.raindrop_xl, null),
                            context.resources.getDrawable(R.drawable.raindrop_2xl, null)
                        )
                    )
                }
                302, 303, 304  -> {
                    EffectLightningDrawable(
                        context.resources.getDrawable(R.drawable.lightning_1, null),
                        context.resources.getDrawable(R.drawable.lightning_2, null)
                    )
                }
                in 400..457 -> {  // 小雪
                    EffectSnowDrawable(
                        0,
                        arrayOf(
                            context.resources.getDrawable(R.drawable.snowflake_tiny, null),
                            context.resources.getDrawable(R.drawable.snowflake_s, null),
                            context.resources.getDrawable(R.drawable.snowflake_m, null),
                            context.resources.getDrawable(R.drawable.snowflake_l, null),
                            context.resources.getDrawable(R.drawable.snowflake_xl, null),
                            context.resources.getDrawable(R.drawable.snowflake_xxl, null)
                        )
                    )
                }
                401, 402, 403, 409, 410-> {  // 大雪
                    EffectSnow2Drawable(
                        0,
                        arrayOf(
                            context.resources.getDrawable(R.drawable.snow_flower_22, null),
                            context.resources.getDrawable(R.drawable.snow_flower_24, null)
                        )
                    )
                }
                else -> {
                    null
                }
            }
        }
    }
}