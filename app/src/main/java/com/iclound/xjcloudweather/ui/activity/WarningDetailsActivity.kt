package com.iclound.xjcloudweather.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.adapter.TopCityAdapter
import com.iclound.xjcloudweather.adapter.WarningDetailAdapter
import com.iclound.xjcloudweather.base.BaseMvpActivity
import com.iclound.xjcloudweather.base.WarningNowDatas
import com.iclound.xjcloudweather.mvp.contract.WarningDetailsContract
import com.iclound.xjcloudweather.mvp.presenter.WarningDetailsPresenter
import com.iclound.xjcloudweather.utils.ContentUtil
import kotlinx.android.synthetic.main.activity_title_base.*
import kotlinx.android.synthetic.main.activity_warning_desc.*

class WarningDetailsActivity : BaseMvpActivity<WarningDetailsContract.View, WarningDetailsContract.Presenter>(),
    WarningDetailsContract.View {

    private var location = ""
    override fun attachLayoutRes(): Int = R.layout.activity_warning_desc

    override fun isHideTitle(): Boolean = false
    override fun prepareData(intent: Intent?) {}

    private val warningDetailAdapter: WarningDetailAdapter by lazy {
        WarningDetailAdapter()
    }

    override fun start() {}

    override fun createPresenter(): WarningDetailsContract.Presenter = WarningDetailsPresenter()

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) //设置状态栏字体为暗色
        }
        super.initView()
        weather_toolbar.run{
            setSupportActionBar(this)
            // 返回键显示默认图标
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // 返回键可用
            supportActionBar?.setHomeButtonEnabled(true)
        }

        location = ContentUtil.CITY_ID
        warning_details_layout.run{
            adapter = warningDetailAdapter
            layoutManager = LinearLayoutManager(this@WarningDetailsActivity, RecyclerView.VERTICAL, false)
        }
        mPresenter?.getWarningInfo(location)
        ContentUtil.CITY_ID = ""
    }

    override fun showWarningInfo(datas: WarningNowDatas) {
        warningDetailAdapter.setList(datas.warning)
    }

}