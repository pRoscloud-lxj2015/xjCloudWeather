package com.iclound.xjcloudweather.perm

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.OnPermissionPageCallback
import com.hjq.permissions.XXPermissions
import com.iclound.xjcloudweather.widgets.PermissionDialog

class PermissionInterceptor : IPermissionInterceptor {

    override fun deniedPermissions(
        activity: Activity?,
        allPermissions: MutableList<String>?,
        deniedPermissions: MutableList<String>?,
        never: Boolean,
        callback: OnPermissionCallback?
    ) {
        callback?.onDenied(deniedPermissions, never)
        if(never)
            showPermissionSettingDialog(activity, allPermissions, deniedPermissions, callback)

    }

    private fun showPermissionSettingDialog(activity: Activity?,
                                            allPermissions: MutableList<String>?,
                                            deniedPermissions: MutableList<String>?,
                                            callback: OnPermissionCallback?) {
        if (activity == null || activity.isFinishing ||
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed
        ) {
            return
        }
        PermissionDialog.newBuilder()
            .setTitle("授权提示")
            .setContent("当前应用缺少必要的权限，该功能暂时无法使用。如若需要，请单击【去开启】按钮前往设置中心进行权限授权。")
            .setRightText("去开启")
            .setCancelAble(false)
            .build()
            ?.setOnConfirmClickListener {
                XXPermissions.startPermissionActivity(activity,
                    deniedPermissions, object : OnPermissionPageCallback {
                        override fun onGranted() {
                            if (callback == null) {
                                return
                            }
                            callback.onGranted(allPermissions, true)
                        }

                        override fun onDenied() {
                        }
                    })
            }
            ?.setOnCancelClickListener {

            }?.show((activity as AppCompatActivity).supportFragmentManager, "permission_dialog")
    }
}