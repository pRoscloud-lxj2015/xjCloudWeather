package com.iclound.xjcloudweather.ui.activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iclound.xjcloudweather.MainActivity
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.adapter.SearchCityAdatper
import com.iclound.xjcloudweather.adapter.TopCityAdapter
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.*
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.event.RefreshViewVisiable
import com.iclound.xjcloudweather.mvp.contract.SearchCityContract
import com.iclound.xjcloudweather.mvp.presenter.SearchCityPresenter
import com.iclound.xjcloudweather.utils.ContentUtil
import com.iclound.xjcloudweather.utils.KeyboardUtils
import kotlinx.android.synthetic.main.activity_addcity.*
import kotlinx.android.synthetic.main.activity_searchcity.*
import kotlinx.android.synthetic.main.item_searchcity.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList

class SearchCityActivity : BaseMvpActivity<SearchCityContract.View, SearchCityContract.Presenter>(),
    SearchCityContract.View {

    private val TAG = "SearchCityActivity"

    private val topCity by lazy { ArrayList<CurTopCity>() }  //热门城市

    private val alreadAddCities by lazy { ArrayList<CityEntity>()}  //已经添加城市

    private val searchCities by lazy {ArrayList<CityBean>() }  //request搜索的城市集

    private val searchWeathers by lazy {ArrayList<SearchTargetCity>()} //request搜索的城市天气数据

    private val contias by lazy {ArrayList<Int>() } //是否添加了

    override fun attachLayoutRes(): Int = R.layout.activity_searchcity

    override fun useEventBus(): Boolean = true
    override fun prepareData(intent: Intent?) {}

    override fun start() {
    }

    override fun createPresenter(): SearchCityContract.Presenter = SearchCityPresenter()

    private val topCityAdapter: TopCityAdapter by lazy {
        TopCityAdapter()
    }

    private val searchCityAdapter: SearchCityAdatper by lazy{
        SearchCityAdatper(R.layout.item_searchcity)
    }

    override fun initView() {
        Log.d(TAG ,"onCreate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) //设置状态栏字体为暗色
        }
        super.initView()
        getTopCity()
        ViewCompat.setTransitionName(search_layout, "add_search_layout")
        cacle_text.setOnClickListener {
            //隐藏键盘
           // hideInputSoftWindow()
            finishAfterTransition()
        }
        top_city_grid.run {
            adapter = topCityAdapter
            layoutManager = GridLayoutManager(this@SearchCityActivity, 3)
            itemAnimator = DefaultItemAnimator()
        }
        //热门城市
        topCityAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as CurTopCity
                itemClick(item)
            }
        }

        //搜索城市
        rc_search.run {
            adapter = searchCityAdapter
            layoutManager = LinearLayoutManager(this@SearchCityActivity, RecyclerView.VERTICAL, false)
        }
        searchCityAdapter.run{
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as CityBean
                itemChildClick(item, view, position)
            }
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as CityBean
                HalfWeatherActivity.start(this@SearchCityActivity, item.cityId, item.cityName, !item.isFavor, item.isFavor, alreadAddCities.size)
            }
        }
        //city_search.requestFocus()
        city_search.addTextChangedListener(object: TextWatcher{  //输入城市搜索
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG , " beforeTextChange")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG , " onTextChanged")
                val keywords = city_search.text.toString()
                if(!TextUtils.isEmpty(keywords)){
                    mPresenter?.getSearchCity(keywords)
                }else{
                    rc_search.visibility = View.GONE
                    ll_history.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG , " afterTextChanged")
            }
        })
    }

    override fun onBackPressed() {
        //hideInputSoftWindow()
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        city_search.requestFocus()
        KeyboardUtils.showSoftInput(city_search, 0)

        Log.d(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onPause() {
        KeyboardUtils.hideSoftInput(this@SearchCityActivity.window)
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun showTopCity(datas: SearchCityDatas) {
        if (datas.code.equals("200")) {
            rc_search.visibility = View.VISIBLE
            ll_history.visibility = View.GONE
            searchCities.clear()
            datas.location.forEach { item ->
                searchCities.add(locationToCityBean(item))
            }
            //Log.d("SeacherCity", " showTopCity:" + searchCities[0].cityName + " :" + searchCities[0].cityId)
            HalfWeatherActivity.start(this@SearchCityActivity, searchCities[0].cityId, searchCities[0].cityName, true, false, alreadAddCities.size )
        }
    }

    override fun showSearchCity(datas: SearchCityDatas) {
        if (datas.code.equals("200")) {
            rc_search.visibility = View.VISIBLE
            ll_history.visibility = View.GONE
            searchCities.clear()
            datas.location.forEach { item ->
                searchCities.add(locationToCityBean(item))
            }

            mPresenter?.getSearchCityWeather(searchCities[0].cityId)
        }
    }

    override fun showSearchCityWeather(datas: DaylyDatas) {
        var count = 0
        searchCities[0].searchCityWeather.clear()
        run loop@{
            datas.daily.forEach { item ->
                if (count == 5) return@loop
                searchCities[0].searchCityWeather.add(SearchTargetCity(item.fxDate, item.tempMax, item.tempMin, item.iconDay, item.iconNight))
                count++
            }
        }
        if(searchCities.isNotEmpty()) {
            //Log.d(TAG ," showSearchCityWeather:" + searchCities[0].cityName)
            searchCityAdapter?.setList(searchCities)
        }
    }

    private fun getTopCity() {
        //GlobalScope.launch(Dispatchers.IO) {
            val stringArray = resources.getStringArray(R.array.top_city)
            val cityIdArray = resources.getStringArray(R.array.top_city_id)
            val cityNameList = stringArray.toList() as ArrayList<String>
            val cityIdList = cityIdArray.toList() as ArrayList<String>
            AppRepoData.getInstance().getCities()?.forEach { item ->
                alreadAddCities.add(item)
            }
            cityIdList[0] = alreadAddCities[0].cityLocation //替换定位城市ID

            for (cityName in alreadAddCities) {
                for ((index, city) in cityIdList.withIndex()) {
                    //Log.d(TAG,"Search::" + " city:" + city + " cityId：" + cityName.cityLocation)
                    if (cityName.cityLocation.contains(city)) {
                        contias.add(index)
                    }
                }
            }

            for ((index, city) in cityNameList.withIndex()) {
                //Log.d(TAG,"Search:" + " city:" + city)
                if(index == 0)
                    topCity.addAll(listOf(CurTopCity( alreadAddCities[0].cityLocation, city, index == 0)))
                else {
                    if(contias.contains(index))
                        topCity.addAll(listOf(CurTopCity(cityIdList[index], city, false, true)))
                    else
                        topCity.addAll(listOf(CurTopCity(cityIdList[index], city, false)))
                }
            }
       // }
        topCityAdapter?.setList(topCity)
    }

    //热门城市点击事件
    private fun itemClick(item: CurTopCity) {
        //hideInputSoftWindow()
        alreadAddCities.forEach{
            if(it.cityLocation.contains(item.cityId)){
                //Log.d(TAG," dbCity:" + it.cityName + " :" + it.cityLocation,)
                HalfWeatherActivity.start(this@SearchCityActivity, it.cityLocation, it.cityName, false, true, alreadAddCities.size )
                return
            }
        }
        HalfWeatherActivity.start(this@SearchCityActivity, item.cityId, item.name, true, false, alreadAddCities.size )
        //mPresenter?.getTopCity(if(item.islocal) item.cityId else item.name)  //不需要请求数据得到id, 直接使用对应cityId 就行
    }

    //搜索城市点击事件
    private fun itemChildClick(item: CityBean, view: View, position: Int) {
        hideInputSoftWindow()
        when(view.id){
            R.id.search_addcity -> {  //添加城市
                if(alreadAddCities.size < ContentUtil.CITY_ADD_COUNT){
                //GlobalScope.launch(Dispatchers.IO){
                    AppRepoData.getInstance().addCity(CityEntity(item.cityId, item.cityName, false))
                    ContentUtil.CITY_CHANGE = true
                    ContentUtil.CITY_ID = item.cityId
                //}
                    view.visibility = View.GONE
                    searchCityAdapter.getViewByPosition(position, R.id.alread_addcity_layout)?.visibility = View.VISIBLE
                    //EventBus.getDefault().post(RefreshViewVisiable(View.VISIBLE))
                    //
                }else{
                    Toast.makeText(this@SearchCityActivity, "最多只能添加10个城市", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.alread_addcity_layout -> { //跳转到7天趋势
                HalfWeatherActivity.start(this@SearchCityActivity, item.cityId, item.cityName, false, true, alreadAddCities.size)
            }
        }

    }

    /**
     * 显示已添加城市
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshViewVisiable(event: RefreshViewVisiable){
        alread_addcity_layout.visibility = event.visiable
    }

    private fun hideInputSoftWindow(){
        val imm = this@SearchCityActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
        (imm as InputMethodManager).hideSoftInputFromWindow(city_search.windowToken, 0)
    }

    private fun locationToCityBean(location: Locations): CityBean {
        var parentCity = location.adm2
        val adminArea = location.adm1
        val city = location.country
        if (TextUtils.isEmpty(parentCity)) {
            parentCity = adminArea
        }
        if (TextUtils.isEmpty(adminArea)) {
            parentCity = city
        }
        val cityName = parentCity + " - " + location.name
        val cityId = location.id
        val cnty = city
        val adminAre = adminArea
        var isFavor = false

        for(i in 0 until alreadAddCities.size){
            if(alreadAddCities[i].cityLocation == cityId){
                isFavor = true
            }
        }

        return CityBean(cityName, cityId, cnty, "", parentCity, adminAre, isFavor, searchWeathers)
    }
}