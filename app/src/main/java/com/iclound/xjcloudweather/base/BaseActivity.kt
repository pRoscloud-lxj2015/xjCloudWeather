package com.iclound.xjcloudweather.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.utils.ContentUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_title_base.*
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {
    /**
     * 布局文件id
     */
    protected abstract fun attachLayoutRes(): Int

    // 权限相关回调，可按需实现
    protected open fun permissionGranted() {}
    protected open fun permissionDenied(permission: String) {}
    protected open fun permissionTip(permission: String) {}

    /**
     * 是否有权限申请
     */
    open fun isApplyOfPermission(): Boolean = false

    /**
     * 是否隐藏标题栏
     */
    open fun  isHideTitle(): Boolean  = true

    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * 是否设置透明
     */
    private fun hideTitleBar(){
        supportActionBar?.hide()
    }

    /**
     * 沉浸式状态栏
     */
    protected open fun immersionStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE )
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 状态栏改为透明
            window.statusBarColor = Color.TRANSPARENT
            //window.navigationBarColor = Color.TRANSPARENT
        }
    }

    abstract fun initView()

    abstract fun prepareData(intent: Intent?)

    abstract fun start()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //初始化MMKV

        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }

        if(isHideTitle())
            hideTitleBar()
        immersionStatusBar()
        initAMap()
        start()
        initView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        prepareData(intent)
    }

    private fun initAMap(){
        AMapLocationClient.updatePrivacyShow(App.instance.applicationContext, true, true);
        AMapLocationClient.updatePrivacyAgree(App.instance.applicationContext, true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //ContentUtil.refresh_BG = true
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}