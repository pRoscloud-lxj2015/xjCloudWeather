package com.iclound.xjcloudweather

import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.iclound.xjcloudweather.adapter.ViewPagerAdapter
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.BaseMvpActivity
import com.iclound.xjcloudweather.base.WeatherDatas
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.event.RefreshWeatherBg
import com.iclound.xjcloudweather.mvp.contract.MainContract
import com.iclound.xjcloudweather.mvp.presenter.MainPresenter
import com.iclound.xjcloudweather.ui.activity.CityManagerActivity
import com.iclound.xjcloudweather.ui.fragment.WeatherFragment
import com.iclound.xjcloudweather.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseMvpActivity<MainContract.View, MainContract.Presenter>(), MainContract.View{

    private val TAG = "MainActiviyy"
    private val fragments: MutableList<Fragment> by lazy { ArrayList() }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun isApplyOfPermission(): Boolean = true

    override fun useEventBus(): Boolean = true

    override fun prepareData(intent: Intent?) {
    }

    override fun start() {

    }

    private var mWeatherFragment: WeatherFragment? = null

    //private var location: Location? = null
    private val cityList = ArrayList<CityEntity>()
    private var mCurIndex = 0
    private var fromSplash = false
    private var currentCode:String? = "" //当前天气

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,  "onCreate")
//        val bundle = intent.extras
//        val lon = bundle?.getString("lon")
//        val lat = bundle?.getString("lat")
//        val cityId = bundle?.getString("cityId")
//        val cityName = bundle?.getString("cityName")
//        val dis = bundle?.getString("dis")
//        val street = bundle?.getString("street")
        //location = Location(cityName.toString(), cityId.toString(), lat.toString(), lon.toString(), dis.toString(), street.toString())
        intent?.let {
            fromSplash = it.getBooleanExtra("fromsplash", false)
        }
        val cityEntity = AppRepoData.getInstance().getCities()
        if(!cityEntity.isNullOrEmpty()){
            cityList.clear()
            cityList.addAll(cityEntity)
        }
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        if(!cityList.isEmpty()){
            showCity("")
        }

        view_pager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(i: Int) {
                iw_loc.visibility = if (cityList[i].isLocation) View.VISIBLE else View.INVISIBLE
                llw_round.getChildAt(mCurIndex).isEnabled = false
                llw_round.getChildAt(i).isEnabled = true
                mCurIndex = i
                tw_location.text = cityList[i].cityName
            }

        })
        iw_bg.setImageDrawable(resources.getDrawable(R.drawable.bg_qintian_d, null))
        iw_add_city.run {
            setOnClickListener {
                //跳转添加城市activity
                startActivity(Intent(this@MainActivity, CityManagerActivity::class.java))
            }
        }
    }

    private fun showCity(addCity: String, jumpPos: Int = -1){
        if(mCurIndex > cityList.size - 1 && jumpPos < 0) {
            mCurIndex = cityList.size - 1
        }
        if(jumpPos >= 0) mCurIndex = jumpPos
        iw_loc.visibility = if(cityList[mCurIndex].isLocation) View.VISIBLE else View.INVISIBLE
        tw_location.setText(cityList[mCurIndex].cityName)
        llw_round.removeAllViews()
        // 宽高参数
        var size = DisplayUtils.dp2px(4f)
        var layoutParams = LinearLayout.LayoutParams(size, size)

        for (i in cityList.indices) {
            // 创建底部指示器(小圆点)
            val view = View(this@MainActivity)
            if(i == 0){
                size = DisplayUtils.dp2px(7f)
                layoutParams = LinearLayout.LayoutParams(size, size)
                view.setBackgroundResource(R.drawable.location_back)
            }else {
                size = DisplayUtils.dp2px(4f)
                layoutParams = LinearLayout.LayoutParams(size, size)
                view.setBackgroundResource(R.drawable.background)
            }
            view.isEnabled = false
            // 设置间隔
            layoutParams.gravity = Gravity.CENTER
            layoutParams.rightMargin = 24
            // 添加到LinearLayout
            llw_round.addView(view, layoutParams)
        }
        // 小白点
        llw_round.getChildAt(mCurIndex).isEnabled = true
        llw_round.visibility = if (cityList.size <= 1) View.GONE else View.VISIBLE

        fragments.clear()
        var isRequest = false

        for(city in cityList){
            val location = city.cityLocation
            isRequest = location == addCity && !city.isLocation
            val weatherFragment = WeatherFragment.newInstance(city.cityName, location, city.isLocation, isRequest or fromSplash)
            fromSplash = false
            fragments.add(weatherFragment)
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        view_pager.offscreenPageLimit = 2
        view_pager.currentItem =  if(addCity.length > 1 && jumpPos < 0) cityList.size else mCurIndex
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity",  " onResume")
        if(ContentUtil.CITY_CHANGE){
            val cityEntity = AppRepoData.getInstance().getCities()
            if(!cityEntity.isNullOrEmpty()){
                cityList.clear()
                cityList.addAll(cityEntity)
                showCity(ContentUtil.CITY_ID, ContentUtil.JUMP_ID)
            }
            ContentUtil.JUMP_ID = -1
            ContentUtil.CITY_CHANGE = false
            ContentUtil.CITY_ID = ""
        }else{
            for(i in 0 until cityList.size){
                if(cityList[i].cityLocation == ContentUtil.CITY_ID) {
                    view_pager.currentItem = i
                    ContentUtil.CITY_ID = ""
                    break
                }
            }
        }

        iw_effect.drawable?.let {
            (it as Animatable).start()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFreshWeatherBack(event: RefreshWeatherBg){
        changWeatherBackground(event.icon)
        //
    }

    private fun changWeatherBackground(condCode: String){
        Log.d(TAG, "weatherBack:"+ mCurIndex + " currentCode:"+ currentCode + " cond:" + condCode)
        if(currentCode == condCode){
            return
        }
        currentCode = condCode
        val bgDrawable = IconUtils.getBackg(this@MainActivity, condCode.toInt())
        val originDrawable = iw_bg.drawable
        val targetDrawable = resources.getDrawable(bgDrawable, null)
        val transitionDrawable = TransitionDrawable(
            arrayOf<Drawable>(originDrawable, targetDrawable)
        )

        iw_bg.setImageDrawable(transitionDrawable)
        //(iw_bg.background as GradientDrawable).setCornerRadius(0.0f)
        transitionDrawable.isCrossFadeEnabled = true
        transitionDrawable.startTransition(1000)

        // 获取特效
        val effectDrawable = EffectUtils.getEffect(App.context, condCode.toInt())
        iw_effect.setImageDrawable(effectDrawable)
    }

    override fun showFragment(datas: WeatherDatas) {

    }

    override fun onDestroy() {
        super.onDestroy()
        iw_effect.drawable?.let {
            (it as Animatable).stop()
        }
    }

}
