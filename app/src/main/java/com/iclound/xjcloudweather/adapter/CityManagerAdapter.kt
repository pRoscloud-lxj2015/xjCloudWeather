package com.iclound.xjcloudweather.adapter

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import com.iclound.xjcloudweather.utils.DisplayUtils
import com.iclound.xjcloudweather.utils.IconUtils
import kotlinx.android.synthetic.main.item_city_manager.view.*

//class CityManagerAdapter(var onSort: ((List<CityWeatherEntity>) -> Unit)? = null) :
class CityManagerAdapter :
    BaseQuickAdapter<CityWeatherEntity, BaseViewHolder>(R.layout.item_city_manager){

    init{
        addChildClickViewIds(R.id.ic_delete)
    }

    override fun convert(holder: BaseViewHolder, item: CityWeatherEntity) {
        //val resId = item.nowIcon?.toInt()?.let { IconUtils.getBackg(context, it) }
        //resId?.let { holder.setBackgroundResource(R.id.citymanage_item_layout, it) }
        if(holder.layoutPosition != 0) {
            val bgDrawable = item.nowIcon?.toInt()?.let { IconUtils.getBackg(context, it) }
            bgDrawable?.let { holder.setBackgroundResource(R.id.citymanage_item_layout, it) }
            (holder.getView<ConstraintLayout>(R.id.citymanage_item_layout).background as GradientDrawable).mutate()
            (holder.getView<ConstraintLayout>(R.id.citymanage_item_layout).background as GradientDrawable).setCornerRadius(
                DisplayUtils.dp2px(16.0f).toFloat()
            )
            //getColorGradient(item.nowIcon, "start", "end")
            holder.setText(R.id.ic_location, item.cityName)
            holder.setVisible(R.id.ic_location_img, item.isLocation)
            Log.d("TAG", " airNow:" + item.airNow)
            if(item.airNow?.contains("null") == true){
                holder.getView<TextView>(R.id.ic_brief_text).visibility = View.GONE
            }else{
                holder.getView<TextView>(R.id.ic_brief_text).visibility = View.VISIBLE
                holder.setText(R.id.ic_brief_text, item.airNow)
            }

            holder.setText(R.id.temp_maxmin, item.tempMaxMin)
            holder.setText(R.id.current_temp, item.tempNow)
        }
    }

//    private fun getColorGradient(icon: String?, start: String, end: String): IntArray?{
//        val sColor = icon?.toInt()?.let { IconUtils.getGradienBg(context, it, start) }
//        val eColor = icon?.toInt()?.let { IconUtils.getGradienBg(context, it, end) }
//        return eColor?.let { sColor?.let { it1 -> intArrayOf(it1, it) } }
//    }

    fun setHeadViewData(data: CityWeatherEntity?){
        val view =  this.headerLayout
        //this.getViewByPosition(this.headerViewPosition, R.layout.item_city_manager)
        val bgDrawable = data?.nowIcon?.toInt()?.let { IconUtils.getBackg(context, it) }
        bgDrawable?.let { view?.citymanage_item_layout?.setBackgroundResource( it) }

        ((view?.citymanage_item_layout as ConstraintLayout) .background as GradientDrawable).mutate()
        ((view.citymanage_item_layout as ConstraintLayout) .background as GradientDrawable).setCornerRadius(DisplayUtils.dp2px(16.0f).toFloat())

        //getColorGradient(item.nowIcon, "start", "end")
        view.ic_location.text = data?.cityName
        view.ic_location_img.visibility = if(data?.isLocation == true) View.VISIBLE else View.GONE
        if(data?.airNow?.contains("null") == true){
            view.ic_brief_text.visibility = View.GONE
        }else{
            view.ic_brief_text.visibility = View.VISIBLE
            view.ic_brief_text.text = data?.airNow
        }
        view.temp_maxmin.text = data?.tempMaxMin
        view.current_temp.text = data?.tempNow
    }
}