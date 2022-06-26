package com.iclound.xjcloudweather.ui.activity

import android.Manifest
import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.core.content.PermissionChecker
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.iclound.xjcloudweather.MainActivity
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.base.BaseActivity
import com.iclound.xjcloudweather.base.Location
import com.iclound.xjcloudweather.event.MapLocationEvent
import com.iclound.xjcloudweather.http.GaodeMapHelper
import com.iclound.xjcloudweather.perm.PermissionInterceptor
import com.iclound.xjcloudweather.utils.NetWorkUtils
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 启动页
 */
class SplashActivity : BaseActivity(){

    //var alphaAnimation: AlphaAnimation? = null
    lateinit var animate: ViewPropertyAnimator
    //private var curLocation: String? = null
    //private var animationFinish: Boolean = false
    override fun attachLayoutRes(): Int = R.layout.activity_splash

    override fun useEventBus(): Boolean = false

    override fun start() {
    }

    override fun initView() {
        xj_logo_text.setColors( 0xff3773b1.toInt(), 0xfff98b10.toInt(), 0xff303e63.toInt(), 0xff8ea0b8.toInt(), 0xff586882.toInt())
        //alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        animate = xj_Logo.animate()
        initPermission()
    }

    override fun prepareData(intent: Intent?) {}

    private fun initPermission() {
        if (checkGPSPermission()) {
            GaodeMapHelper.getCurrentLocation()
            startAnimation()
        } else {
            XXPermissions.with(this).permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .interceptor(PermissionInterceptor())
                .request(object : OnPermissionCallback{
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            GaodeMapHelper.getCurrentLocation(true)
                            startAnimation()
                        }
                    }
                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        //Toast
                        Toast.makeText(this@SplashActivity, "无法定位当前位置，请开启定位功能", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun startAnimation() {
        animate.apply {
            duration = 1500L
            startDelay = 30
            interpolator = AccelerateInterpolator()
            translationYBy(-80F)
            scaleXBy(0.2F)
            scaleYBy(0.2F)
        }
        animate.setListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
               // TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator?) {
                jumpToMain()
            }

            override fun onAnimationCancel(animation: Animator?) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator?) {
               /// TODO("Not yet implemented")
            }

        })
//        alphaAnimation?.apply {
//            duration = 2000
//            setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationStart(p0: Animation?) {
//
//                }
//
//                override fun onAnimationEnd(p0: Animation?) {
//                    jumpToMain()
//                }
//
//                override fun onAnimationRepeat(p0: Animation?) {
//                }
//            })
//        }
//        xj_Logo.startAnimation(alphaAnimation)
    }

    private fun jumpToMain() {
        //if(curLocation?.length!! > 1 && animationFinish) {
            if (NetWorkUtils.isConnected()) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fromsplash", true)
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                Toast.makeText(this, "无网络连接,请检查网络", Toast.LENGTH_SHORT).show()
            }
       // }
    }


    /**
     * 检查GPS权限
     */
    fun checkGPSPermission(): Boolean {
        val pm1 = hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val pm2 = hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return (pm1 || pm2)
    }

    private fun hasPermission(context: Context, vararg permissions: String): Boolean {
        if (permissions.isEmpty()) {
            return false
        }
        for (per in permissions) {
            val result = PermissionChecker.checkSelfPermission(
                context,
                per
            )
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMapLocationEvent(mapLocation: MapLocationEvent) {
        //curLocation = mapLocation.location
    }

    override fun onDestroy() {
        super.onDestroy()
        animate.cancel()
        //alphaAnimation?.cancel()
    }


}