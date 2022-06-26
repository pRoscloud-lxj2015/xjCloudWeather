package com.iclound.xjcloudweather.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iclound.xjcloudweather.MainActivity
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.adapter.Forecast7dAdapter
import com.iclound.xjcloudweather.adapter.TopCityAdapter
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.BaseMvpActivity
import com.iclound.xjcloudweather.base.DaylyDatas
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.extend.setNewOrAddData
import com.iclound.xjcloudweather.mvp.contract.HalfWeatherContract
import com.iclound.xjcloudweather.mvp.presenter.HalfWeatherPresenter
import com.iclound.xjcloudweather.utils.ContentUtil
import kotlinx.android.synthetic.main.activity_halfweather.*
import kotlinx.android.synthetic.main.activity_title_base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull

class HalfWeatherActivity : BaseMvpActivity<HalfWeatherContract.View, HalfWeatherContract.Presenter>(), HalfWeatherContract.View {
    @NotNull
    private var location: String = ""

    private var isAddCity: Boolean = false

    private var isAlreadyAddCity: Boolean = false

    private var addCount: Int = 1

    private var position: Int = 0  //显示背景的位置

    @NotNull
    private var cityName: String = ""

    companion object {

        fun start(context: Context?, location: String, cityName: String, isAddCity: Boolean, isAlread: Boolean = false, addCount: Int = 1, pos: Int = 0, bundle: Bundle? = null) {
            Intent(context, HalfWeatherActivity::class.java).run {
                putExtra("location_lonlat", location)
                putExtra("addcity", isAddCity)
                putExtra("cityName", cityName)
                putExtra("alreadyadd", isAlread)
                putExtra("addCount", addCount)
                putExtra("pos", pos)
                context?.startActivity(this, bundle)
            }
        }

//        fun start(context: Context?, location: String) {
//            start(context, location)
//        }

    }

    override fun attachLayoutRes(): Int = R.layout.activity_halfweather

    override fun start() {

    }

    override fun createPresenter(): HalfWeatherContract.Presenter = HalfWeatherPresenter()

    override fun isHideTitle(): Boolean = false
    override fun prepareData(intent: Intent?) {
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val forecast7dAdapter: Forecast7dAdapter by lazy {
        Forecast7dAdapter()
    }

   // private var forecast7dAdapter: Forecast7dAdapter? = null


    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) //设置状态栏字体为暗色
        }
        super.initView()
        intent.extras?.let {
            location = it.getString("location_lonlat").toString()
            isAddCity = it.getBoolean("addcity")
            isAlreadyAddCity = it.getBoolean("alreadyadd")
            cityName = it.getString("cityName").toString()
            addCount = it.getInt("addCount")
            position = it.getInt("pos")
        }

        if(isAlreadyAddCity){
            addcity_layout.visibility = View.VISIBLE
            addcity_btn.setImageResource(R.drawable.icon_add_city_back)
            addcity_desc.text = "前往主页查看"
            weather_title.text = cityName
            location?.let { mPresenter?.requestHalfWeatherData(it) }
        }else{
            if(isAddCity) {
                addcity_layout.visibility = View.VISIBLE
                addcity_btn.setImageResource(R.drawable.icon_search_add_city)
                addcity_desc.text = "添加到主页"
                weather_title.text = cityName
                location?.let { mPresenter?.requestHalfWeatherData(it) }
            } else {
                addcity_layout.visibility = View.GONE
                weather_title.text = "7天趋势预报"  //直接加载缓存数据
                location?.let {
                    mPresenter?.loadHalfWeatherData(it)
                }
            }

        }
        weather_toolbar.run{
            setSupportActionBar(this)
            // 返回键显示默认图标
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // 返回键可用
            supportActionBar?.setHomeButtonEnabled(true)
        }
        iw_7dweather.run {
            layoutManager = LinearLayoutManager(App.context, RecyclerView.HORIZONTAL, false)
            adapter = forecast7dAdapter
            itemAnimator = DefaultItemAnimator()
        }

        forecast7dAdapter.run {
            pos = position
        }
        addcity_btn.setOnClickListener {
            if(isAlreadyAddCity){
                ContentUtil.CITY_ID = location
                startActivity(Intent(this@HalfWeatherActivity, MainActivity::class.java))
                finish()
            }else if(addCount < ContentUtil.CITY_ADD_COUNT){
                //GlobalScope.launch(Dispatchers.IO){
                    AppRepoData.getInstance().addCity(CityEntity(location, cityName, false))
                    ContentUtil.CITY_CHANGE = true
                    ContentUtil.CITY_ID = location
                    startActivity(Intent(this@HalfWeatherActivity, MainActivity::class.java))
                    finish()
               //}
            }else{
                Toast.makeText(this@HalfWeatherActivity, "最多只能添加10个城市", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun showSevData(datas: DaylyDatas) {
        //forecast7dAdapter = Forecast7dAdapter(this, datas.daily)
        var min = datas.daily[0].tempMin.toInt()
        var max = datas.daily[0].tempMax.toInt()
        datas.daily.forEach {
            min = Math.min(it.tempMin.toInt(), min)
            max = Math.max(it.tempMax.toInt(), max)
        }
        forecast7dAdapter?.setRange(min, max)
        forecast7dAdapter?.setList(datas.daily)
        //iw_7dweather.adapter = forecast7dAdapter

        //forecast7dAdapter?.notifyDataSetChanged()
//        forecast7dAdapter.setNewOrAddData(false, datas.daily)
    }



}