package com.iclound.xjcloudweather.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.util.toAndroidPair
import androidx.recyclerview.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.util.getItemView
import com.iclound.xjcloudweather.MainActivity
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.adapter.CityManagerAdapter
import com.iclound.xjcloudweather.base.BaseMvpActivity
import com.iclound.xjcloudweather.base.onItemDragSwipListener
import com.iclound.xjcloudweather.db.AppRepoData
import com.iclound.xjcloudweather.db.entity.CityEntity
import com.iclound.xjcloudweather.db.entity.CityWeatherEntity
import com.iclound.xjcloudweather.mvp.contract.CityManagerContract
import com.iclound.xjcloudweather.mvp.presenter.CityManagerPresenter
import com.iclound.xjcloudweather.utils.ContentUtil
import com.iclound.xjcloudweather.widgets.DragSwipeItemTouchCallback
import kotlinx.android.synthetic.main.activity_addcity.*
import kotlinx.android.synthetic.main.activity_searchcity.*
import kotlinx.android.synthetic.main.activity_title_base.*

class CityManagerActivity : BaseMvpActivity<CityManagerContract.View, CityManagerContract.Presenter>(), CityManagerContract.View{

    val otherDatas by lazy {ArrayList<CityWeatherEntity>() }

    //private val cityDatas by lazy { ArrayList<CityEntity>() }

    private var headData:CityWeatherEntity? = null

    private var isDelete: Boolean = false

    private var mFlingView: View? = null
    private var mTouchFrame: Rect? = null
    private var mPosition = 0
    private var mScrollX = 0
    private var isResetViewItem = true

    override fun attachLayoutRes(): Int = R.layout.activity_addcity

    override fun createPresenter(): CityManagerContract.Presenter = CityManagerPresenter()

    override fun isHideTitle(): Boolean = false

    override fun prepareData(intent: Intent?) {}

    private val cityManagerAdapter: CityManagerAdapter by lazy{
        CityManagerAdapter()
    }

    private val headView: View  by lazy {
       layoutInflater.inflate(R.layout.item_city_manager, rv_top_city, false)
    }

    private val mDragSwipeItemTouchCallback  by lazy{
        DragSwipeItemTouchCallback(cityManagerAdapter)
    }
    private val itemHelper by lazy{
        ItemTouchHelper(mDragSwipeItemTouchCallback)
    }

    private fun headView(data: CityWeatherEntity){
        cityManagerAdapter.setHeadViewData(data)
    }

    override fun start() {
    }

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

        rv_top_city.run {
            layoutManager = LinearLayoutManager(this@CityManagerActivity, RecyclerView.VERTICAL, false)
            adapter = cityManagerAdapter
            itemAnimator = DefaultItemAnimator()
        }
        itemHelper.attachToRecyclerView(rv_top_city)
        cityManagerAdapter.run {
            addHeaderView(headView)
            setOnItemClickListener(object: OnItemClickListener{  //点击定位城市之外的城市
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                    if(isResetViewItem) {
                        ContentUtil.CITY_CHANGE = false
                        ContentUtil.CITY_ID = (adapter.getItem(position) as CityWeatherEntity).cityId
                        ContentUtil.JUMP_ID = 0
                        startActivity(Intent(this@CityManagerActivity, MainActivity::class.java))
                        finish()
                       // Toast.makeText(this@CityManagerActivity, "点击了" + position, Toast.LENGTH_SHORT).show()
                    }else{
                        isResetViewItem = true
                    }
                }
            })
            setOnItemChildClickListener(object: OnItemChildClickListener{  //删除城市
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                    (view.parent as ViewGroup)?.scrollTo(0, 0)
                    RemoveCity(position, (adapter.getItem(position) as CityWeatherEntity).cityId)
                }
            })
        }

        headView.setOnClickListener(object: View.OnClickListener {  //定位城市点击事件
            override fun onClick(v: View?) {
                ContentUtil.CITY_CHANGE = false
                ContentUtil.CITY_ID = headData?.cityId.toString()
                ContentUtil.JUMP_ID = 0
                startActivity(Intent(this@CityManagerActivity, MainActivity::class.java))
                finish()
            }
        })

        search_btn.setOnClickListener(){
            Log.d("CityManagerActivity", " scrollX: "+ mDragSwipeItemTouchCallback.swipItemView?.scrollX)
            if( mDragSwipeItemTouchCallback.swipItemView != null && mDragSwipeItemTouchCallback.swipItemView?.scrollX!! > 0){
                mDragSwipeItemTouchCallback.swipItemView?.scrollTo(0, 0) //复原
                mDragSwipeItemTouchCallback.swipItemView = null
            }
            val searchLayoutPair = Pair<View, String>(add_search_layout, "add_search_layout").toAndroidPair()
            val rvTopCityPair = Pair<View, String>(rv_top_city, "rv_top_city").toAndroidPair()
            val options = ActivityOptions.makeSceneTransitionAnimation(this@CityManagerActivity, searchLayoutPair, rvTopCityPair)
            //makeSceneTransitionAnimation(this, searchLayoutPair, rvTopCityPair)
            startActivity(Intent(this@CityManagerActivity, SearchCityActivity::class.java), options.toBundle())
        }

        rv_top_city.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val result = false
                if(e.action == MotionEvent.ACTION_DOWN){
                    val view = mFlingView
                    val curPosition = pointToPosition(rv, e.x.toInt(), e.y.toInt()) //- cityManagerAdapter.headerLayoutCount
                    mPosition = curPosition
                    mFlingView = rv.getChildAt(mPosition)
                    val curScrollX = mScrollX
                    if(mFlingView != null && view != null) {
                        mScrollX = view?.scrollX
                        Log.d("CityManagerActivity","inter:" + " " + mPosition + " mFL:" + (mFlingView != view) + " mScrollX:" + mScrollX + " curScrollX:" + curScrollX)
                    }
                    Log.d("CityManagerActivity","inter:" +" " + mPosition + " mFL:" + (mFlingView != view)+ " mScrollX:" + mScrollX + " curScrollX:" + curScrollX )
                    if(view != null && mFlingView != view && view.scrollX != 0){
                        view.scrollTo(0, 0)
                        isResetViewItem = false
                        //result = true
                    }
                }

                return result
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                Log.d("CityManagerActivity", "onTouchEvent:" + e.action)
                //TODO("Not yet implemented"):
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                //TODO("Not yet implemented")
            }

        })

        initData()
        getCityManagerData()
    }


    private fun pointToPosition(recyc: RecyclerView, x: Int, y: Int): Int{
        var frame = mTouchFrame
        if(frame == null){
            mTouchFrame = Rect()
            frame = mTouchFrame
        }
        val count = recyc.childCount
        for(i in count-1 downTo 0){
            val child = recyc.getChildAt(i)
            if(child.visibility == View.VISIBLE){
                child.getHitRect(frame)
                if(frame!!.contains(x, y)){
                    return i
                }
            }
        }
        return -1
    }

    private fun initData(){
        mDragSwipeItemTouchCallback.setOnItemDragSwipListener(object: onItemDragSwipListener{
            override fun onItemDragEnd(weatherData: List<CityWeatherEntity>) {
                mPresenter?.updateCityManagerData(weatherData, headData!!)
            }
        })
    }

    override fun showCityManager(data: List<CityWeatherEntity>) {
//        if(isDelete){
//            isDelete = false
//            return
//        }
//        headData = data[0]
//        headView(data[0]) //设置HeadView数据
//        var count = 0
//        data.forEach { item ->
//            if(count != 0)
//                otherDatas.add(item)
//            count++
//        }
//        cityManagerAdapter.setList(otherDatas)
    }

    private fun getCityManagerData(){
        val datas = AppRepoData.getInstance().getCityWeater()
        headData = datas[0]
        headView(datas[0]) //设置HeadView数据
        var count = 0
        datas.forEach { item ->
            if(count != 0)
                otherDatas.add(item)
            count++
        }
        cityManagerAdapter.setList(otherDatas)
    }

    override fun finishCities(datas: List<CityEntity>) {
       // cityDatas.clear()
       // cityDatas.addAll(datas)
    }

    override fun finishSwapCities(data: List<CityWeatherEntity>) {
        AppRepoData.getInstance().removeAllCity()
        AppRepoData.getInstance().addCity(CityEntity(headData?.cityId!!, headData?.cityName!!, headData?.isLocation!!))
        data.forEach{ item ->
            AppRepoData.getInstance().addCity(CityEntity(item.cityId, item.cityName, item.isLocation))
        }
        ContentUtil.CITY_CHANGE = true
    }

    private fun RemoveCity(pos: Int, cityId: String){
        //GlobalScope.launch(Dispatchers.IO){
        AppRepoData.getInstance().removeCityWeather(cityId)
        AppRepoData.getInstance().removeCity(cityId)
        AppRepoData.getInstance().deleteCache("WeatherDatas_" + cityId)
        AppRepoData.getInstance().deleteCache("AirDailyDatas_" + cityId)
        AppRepoData.getInstance().deleteCache("HourlyDatas_" + cityId)
        AppRepoData.getInstance().deleteCache("DaylyDatas_" + cityId)
        AppRepoData.getInstance().deleteCache("WarningNowDatas_" + cityId)
        // }
        cityManagerAdapter.data.removeAt(pos)
        //cityManagerAdapter.notifyItemRemoved(pos + cityManagerAdapter.headerViewPosition)
        cityManagerAdapter.notifyDataSetChanged()
        ContentUtil.CITY_CHANGE = true
        isDelete = true
    }


    private fun getCityData(){
        val weatherData = AppRepoData.getInstance().getCityWeater()
        headView(weatherData[0]) //设置HeadView数据
        var count = 0
        weatherData.forEach { item ->
            if(count != 0)
                otherDatas.add(item)
            count++
        }
        cityManagerAdapter.setList(otherDatas)
    }


    private fun updateRoomCityWeatherData(weatherDatas: List<CityWeatherEntity>){
        AppRepoData.getInstance().removeAllCityWeather()
        headData?.let { AppRepoData.getInstance().addCityWeather(it) }
        weatherDatas.forEach {
            AppRepoData.getInstance().addCityWeather(it)
        }
        finishSwapCities(weatherDatas)
    }

}