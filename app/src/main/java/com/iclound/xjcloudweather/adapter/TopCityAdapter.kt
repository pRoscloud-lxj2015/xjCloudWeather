package com.iclound.xjcloudweather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.base.CurTopCity

class TopCityAdapter : BaseQuickAdapter<CurTopCity, BaseViewHolder>(R.layout.item_top_city),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: CurTopCity) {
        if(holder.layoutPosition == 0 || item.isAdd) {
            holder.setText(R.id.tvCityName, item.name)
            holder.setTextColorRes(R.id.tvCityName, R.color.select_topcity)
        }else{
            holder.setText(R.id.tvCityName, item.name)

        }
        holder.setVisible(R.id.top_location, item.islocal)
    }
}