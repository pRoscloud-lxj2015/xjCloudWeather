package com.iclound.xjcloudweather.base

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 多级嵌套RecycleView
 */
abstract class BaseMultiAdapter<T>(val layoutId: Int) : BaseQuickAdapter<T, BaseViewHolder>(layoutId) {

    abstract fun getItemLayoutId(): Int

}