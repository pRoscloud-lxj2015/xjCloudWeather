package com.iclound.xjcloudweather.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.iclound.xjcloudweather.MainActivity
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.adapter.Forecast3dAdapter
import com.iclound.xjcloudweather.adapter.HourlyAdapter
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import com.iclound.xjcloudweather.event.RefreshWeatherBg
import com.iclound.xjcloudweather.extend.setNewOrAddData
import com.iclound.xjcloudweather.http.GaodeMapHelper
import com.iclound.xjcloudweather.mvp.contract.HomeContract
import com.iclound.xjcloudweather.mvp.presenter.HomePresenter
import com.iclound.xjcloudweather.ui.activity.HalfWeatherActivity
import com.iclound.xjcloudweather.ui.activity.WarningDetailsActivity
import com.iclound.xjcloudweather.utils.*
import com.iclound.xjcloudweather.widgets.CloudSwipeRefreshLayout
import com.iclound.xjcloudweather.widgets.SpaceItemDecoration
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_weather_layout.*
import kotlinx.android.synthetic.main.layout_forecast_hourly.*
import kotlinx.android.synthetic.main.layout_today_brief_info.*
import kotlinx.android.synthetic.main.warning_infor_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import org.greenrobot.eventbus.EventBus
import java.time.temporal.ValueRange
import java.util.*

/**
 * 天气页面
 */
class WeatherFragment : BaseMvpFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {
    private val TAG: String = "WeatherFragment"
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: Boolean, param4: Boolean) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_CITY_NAME, param1)
                    putString(PARAM_CITY_ID, param2)
                    putBoolean(PARAM_CITY_LOCAL, param3)
                    putBoolean("isRequestData", param4)
                }
            }
    }
    private val PARAM_CITY_ID = "param_city_local"
    private val PARAM_CITY_LOCAL = "param_city_local_flag"
    private val PARAM_CITY_NAME = "param_city_name"

   // var curLocation: Location? = null

    private lateinit var mCityIcon: String
    private lateinit var mCityName: String
    private lateinit var mCityLocation: String
    private  var mCityAirNow: String? = null
    private  var mCityTempMax: String? = null
    private  var mCityTemp: String? = null

    private var isLocal: Boolean = false
    private var isRequestData: Boolean = false
    private var condCode: String? = null

   // private var dayNum = 0

    override fun createPresenter(): HomePresenter = HomePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_weather_layout

    override fun useEventBus(): Boolean = false

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val forecast3dAdapter: Forecast3dAdapter by lazy {
        Forecast3dAdapter()
    }

    private val forecastHourlyAdapter: HourlyAdapter by lazy{
        HourlyAdapter()
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    private fun loadCacheWeather(location: String){
        Log.d("WeatherFragment","loadCacheWeather")
        mPresenter?.loadCacheRealData(location)
        mPresenter?.loadCacheDayData(location)
        mPresenter?.loadCacheHourlyData(location)
        mPresenter?.loadCacheAirNowData(location)
        mPresenter?.loadCacheWarningNowData(location)
    }

    //刷新时调用接口，刷新数据
    private fun loadWeatherData(location: String){
        mPresenter?.requestWeatherData(location)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG ,  " setUserVisibleHint: " + isVisibleToUser.toString() + " isLocal:" + isLocal)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG ,  " onStart")
        //加载缓存
        if(RefreshUtils.isAlreadyWeatherData(mCityLocation))
            loadCacheWeather(mCityLocation)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        tw_Date.text = "${(Calendar.getInstance().get(Calendar.MONTH) + 1).toString()}月${day}日 农历${LunarUtils.lunarOfDays()}"
    }

    override fun onResume() {
        super.onResume()
        //刷新背景
        condCode?.let {
            EventBus.getDefault().post(RefreshWeatherBg(it))
        }
        Log.d(TAG , " onResume:" + " isLocal:" + isLocal)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG ,  " onCreate")
        arguments?.let {
            val name = it.getString(PARAM_CITY_NAME).toString()
            val list = name.split(Regex("-"))
            mCityName = if(list.size > 1) list[1] else list[0]
            mCityLocation = it.getString(PARAM_CITY_ID).toString()
            isLocal = it.getBoolean(PARAM_CITY_LOCAL)
            isRequestData = it.getBoolean("isRequestData")
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        //weather_swipe_layout.setProgressViewOffset(true, -50, -50)
        tw_forecast3.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = forecast3dAdapter
            itemAnimator = DefaultItemAnimator()
        }
        tw_forecasthourly.run{
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = forecastHourlyAdapter
            itemAnimator = DefaultItemAnimator()
        }
        btn_15day.setOnClickListener {
            HalfWeatherActivity.start(activity, mCityLocation, mCityName, false, false)
        }

        forecast3dAdapter.run {
            setOnItemClickListener(object: OnItemClickListener{
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int
                ) {
                    HalfWeatherActivity.start(activity, mCityLocation, mCityName, false, false, 1, position)
                }

            })
        }

        weather_swipe_layout.setOnRefreshListener(object: CloudSwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                if(NetWorkUtils.isConnected()){
                    if(RefreshUtils.hasRefreshData(mCityLocation))
                        loadWeatherData(mCityLocation)
                    else
                        loadCacheWeather(mCityLocation)
                }else{
                    Toast.makeText(context, "无网络连接,请检查网络", Toast.LENGTH_SHORT).show()
                    weather_swipe_layout.isRefreshing = false
                }
            }
        })
    }

    override fun lazyLoad() {
        if(RefreshUtils.hasRefreshData(mCityLocation)){
            weather_swipe_layout.isRefreshing = true
            loadWeatherData(mCityLocation)
        }
    }

    override fun showFragment(datas: WeatherDatas) {
        weather_swipe_layout.isRefreshing = false
        mCityTemp= "${datas.now.temp}°"
        mCityIcon = datas.now.icon
        tw_today_temp.text = datas.now.temp
        tv_today_cond.text = datas.now.text
        iw_today_wind.text = datas.now.windDir +" "+ datas.now.windScale + "级"
        iw_today_humidity.text = "湿度 " + datas.now.humidity + "%"
        condCode = datas.now.icon
        if(isLocal) EventBus.getDefault().post(RefreshWeatherBg(datas.now.icon))
    }

    override fun showThreeDayFragment(datas: DaylyDatas) {
        mCityTempMax = "${datas.daily[0].tempMax}° / ${datas.daily[0].tempMin}°"
        var count = 0
        val threadDatas by lazy {ArrayList<DaylyDatas>(1) }
        val dailys by lazy {ArrayList<Daily>(3) }
        datas.daily.forEach { item ->
            if (count <= 2){
                dailys.add(Daily(item.cloud, item.fxDate, item.humidity, item.iconDay, item.iconNight, item.moonPhase,
                    item.moonPhaseIcon, item.moonrise, item.moonset, item.precip, item.pressure, item.sunrise, item.sunset, item.tempMax,
                    item.tempMin, item.textDay, item.textNight, item.uvIndex, item.vis, item.wind360Day, item.wind360Night, item.windDirDay,
                    item.windDirNight, item.windScaleDay, item.windScaleNight, item.windSpeedDay, item.windSpeedNight))
            }
             count++
        }
        threadDatas.add(DaylyDatas(datas.code, dailys, datas.fxLink, datas.refer, datas.updateTime))
        forecast3dAdapter.setList(threadDatas[0].daily)
        Log.d(TAG , "ThreadDay:" + mCityTempMax+ " mCityLocation:" + mCityLocation)

    }

    override fun showHourlyFragment(datas: HourlyDatas) {
        forecastHourlyAdapter.setList(datas.hourly)//(setNewOrAddData(false, datas.hourly)
        val precip = datas.hourly[0].precip //当前小时累计降水量
        val pop = datas.hourly[0].pop //逐小时预报降水概率
        var result = ""
        var briefDrawable = 0
        if(!pop.contains("null") && pop.toFloat() <= 0 && precip.toFloat() <= 0){
            //icon出现下雨， 则提示正在下 * 雨
            val rain = WeatherUtils.whetherRains(datas.hourly[0].icon.toInt())
            if(rain.equals("")){
                //2小时内无降雨
                result = "2小时内无降雨"
                briefDrawable = R.drawable.rain_fall_normal
            }else{
                // 正在下 rain
                result = "正在下$rain"
                briefDrawable = WeatherUtils.whetherRainsResId(datas.hourly[0].icon.toInt())
            }
        }else if(!pop.contains("null") && pop.toInt() > 0){
            //降水概率 pop.toInt() %
            if(pop.toInt() > 50) {
                result = "降雨概率 ${pop.toInt()}%"
                briefDrawable = R.drawable.rain_fall_heavy_rain
            }else{
                result = "2小时内无降雨"
                briefDrawable = R.drawable.rain_fall_normal
            }

        }else if(precip.toInt() > 0){
            datas.hourly[0].icon
            val rain = WeatherUtils.whetherRains(datas.hourly[0].icon.toInt())
            //正在降 *雨
            result = "正在下$rain"
            briefDrawable = WeatherUtils.whetherRainsResId(datas.hourly[0].icon.toInt())
        }
        tw_rainfall_text.text =result

        tw_rainfall_brief.setImageResource(briefDrawable)
        (tw_rainfall_brief as ImageView).drawable.setTint(ContextCompat.getColor(App.context, R.color.white))

    }

    override fun showAirNow(data: AirDailyDatas){
        if(data.now?.category == null){
            biref_item.visibility = View.GONE
            rainfall_layout.visibility = View.GONE
        }else{
            biref_item.visibility = View.VISIBLE
            rainfall_layout.visibility = View.VISIBLE
            val aqi = data.now.aqi.toInt()
            var brief = R.drawable.realtime_aqi_leaf
            if(aqi<= 100){
                mCityAirNow = "空气" + data.now?.category
            }else if(aqi in 101..200){ //轻中度污染
                mCityAirNow = data.now.category
                brief = R.drawable.realtime_aqi_skull
            }else{
                mCityAirNow = data.now.category
                brief = R.drawable.realtime_aqi_gas_mask
            }
            tw_feel_text.setText(mCityAirNow+ " " + data.now?.aqi)
            tw_feel_brief.setImageResource(brief)
            tw_feel_brief.drawable.setTint(ContextCompat.getColor(App.context, R.color.white))
        }
    }

    private fun insetDatabase(){
        Log.d(TAG , "CityWeather:"+ mCityName + mCityAirNow + mCityTempMax + mCityTemp + isLocal)
        //GlobalScope.launch(Dispatchers.IO) {
            AppRepoData.getInstance().addCityWeather(
                CityWeatherEntity(mCityName, mCityLocation,  mCityAirNow, mCityTempMax, mCityTemp, mCityIcon, isLocal)
            )
       // }
    }

    override fun showWarningNow(datas: WarningNowDatas) {
        if(datas.warning.size > 0){
            tw_alarmFlipper.visibility = View.VISIBLE
            tw_alarmFlipper.setInAnimation(requireContext(), R.anim.bottom_in)
            tw_alarmFlipper.setOutAnimation(requireContext(), R.anim.top_out)
            tw_alarmFlipper.flipInterval = 4000
            for(warning in datas.warning){
                val level: String = WeatherUtils.getWarningLevelStr(warning.severityColor)
                val tip = warning.typeName + level + "预警:  "
                val view: View = layoutInflater.inflate(R.layout.warning_infor_layout, null)
                view.warning_img.setImageResource(IconUtils.getDayIcon(requireContext(), "icon_" + warning.type))
                view.warning_img.background = WeatherUtils.getWarningRes(requireContext(), warning.severityColor).first
                view.warning_text.text = tip + warning.text
                view.warning_info_layout.setOnTouchListener(object:View.OnTouchListener{
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {  //设置ViewFlipper效果
                        when(event?.action){
                            MotionEvent.ACTION_DOWN -> {
                                tw_alarmFlipper.scaleX = 0.97f
                                tw_alarmFlipper.scaleY = 0.97f
                            }
                            MotionEvent.ACTION_UP -> {
                                tw_alarmFlipper.scaleY = 1f
                                tw_alarmFlipper.scaleX = 1f
                            }
                        }
                        return false
                    }
                })
                view.warning_info_layout.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(v: View?) {
                        ContentUtil.CITY_ID = mCityLocation
                        startActivity(Intent(activity, WarningDetailsActivity::class.java))
                    }

                })
                tw_alarmFlipper.addView(view)
            }
        }else{
            tw_alarmFlipper.visibility = View.GONE
        }
        if(datas.warning.size > 1){
            tw_alarmFlipper.startFlipping()
        }else{
           if((tw_alarmFlipper.isFlipping)){
               tw_alarmFlipper.stopFlipping()
           }
        }
    }

    override fun finishRequest() {
        insetDatabase()
        weather_swipe_layout.isRefreshing = false
    }

    private fun setRainfall(precip: String){
        val text = WeatherUtils.whetherRains(precip.toInt())
        if(!text.equals(""))
            tw_rainfall_text.text = "正在下$text"
        else
            tw_rainfall_text.text = "2小时内无降水"
    }


}