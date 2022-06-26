package com.iclound.xjcloudweather.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.base.BaseMultiAdapter
import com.iclound.xjcloudweather.base.CityBean
import com.iclound.xjcloudweather.base.SearchTargetCity

/**
 * 用于显示搜索城市 adapter
 * 添加多层嵌套adpater
 */
class SearchCityAdatper(layoutId: Int) : BaseMultiAdapter<CityBean>(layoutId){
//    private var searchCityWeatherAdapter: SearchCityWeatherAdapter by lazy{
//        SearchCityWeatherAdapter(R.layout.item_search_forecast5)
//    }
    init {
        addChildClickViewIds(R.id.search_addcity)
        addChildClickViewIds(R.id.alread_addcity_layout)
    }

    override fun convert(holder: BaseViewHolder, item: CityBean) {
        val name = item.cityName

        val x = name.indexOf("-")
        val parentCity = name.substring(0, x)
        val location = name.substring(x + 1)
        var cityName = location + " - " + parentCity + "，" + item.adminArea + "，" + item.cnty
        if (TextUtils.isEmpty(item.adminArea)) {
            cityName = location + "，" + parentCity + "，" + item.cnty
        }
        if(!TextUtils.isEmpty(cityName)){
            holder.setText(R.id.svCity, cityName)
        }

        if(holder.layoutPosition == 0){
            if(!item.isFavor){
                holder.setImageResource(R.id.search_addcity, R.drawable.icon_search_add_city)
            }
            holder.setGone(R.id.sw_forecast7, false)
            (holder.getView(R.id.sw_forecast7) as RecyclerView).run{
                layoutManager = GridLayoutManager(context, 5)
                adapter = SearchCityWeatherAdapter(R.layout.item_search_forecast5)
            }
            ((holder.getView(R.id.sw_forecast7) as RecyclerView)?.adapter as SearchCityWeatherAdapter)?.addData(item.searchCityWeather)
            holder.setVisible(R.id.search_addcity_line, true)
        }else  {
            //holder.setGone(R.id.sw_forecast7, true)
            if(!item.isFavor){
                holder.setImageResource(R.id.search_addcity, R.drawable.icon_search_add_city_no)
            }
            holder.setVisible(R.id.search_addcity_line, false)
        }
        if(item.isFavor){  //该城市已经添加了，则不显示+
            holder.getView<LinearLayout>(R.id.alread_addcity_layout)?.visibility = View.VISIBLE
            holder.getView<ImageView>(R.id.search_addcity)?.visibility = View.GONE
        }
    }

    override fun getItemLayoutId(): Int = R.layout.item_searchcity


}