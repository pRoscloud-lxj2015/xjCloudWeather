package com.iclound.xjcloudweather.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.iclound.xjcloudweather.base.IPermissionScope
import com.iclound.xjcloudweather.widgets.PermissionDialog


object PermissionsHelper {

    fun ActivityResultCaller.registerForPermissionResult(
        onGranted: ((String) -> Unit)? = null,
        onDenied: ((IPermissionScope, String) -> Unit)? = null,
        onShowRequestRationale: ((IPermissionScope, String) -> Unit)? = null
    ): ActivityResultLauncher<String> {
        val appSettingLauncher = appSettingsLauncher()
        val currentApplyLauncher = currentApplyLauncher(appSettingLauncher, onGranted)
        return registerForActivityResult(RequestPermissionContract()){ result ->
//            val permissionScope = generatorDefaultScope(
//
//            )


        }
    }

    fun ActivityResultCaller.currentApplyLauncher(
        appSettingLauncher: ActivityResultLauncher<Unit>,
        callback: ((String) -> Unit)? = null
    ) = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            Log.d("PermissionsHelper","Request Permission ACCESS_BACKGROUND_LOCATION Success")
            callback?.let { it("android.permission.ACCESS_FINE_LOCATION") }
        } else {
            Log.d("PermissionsHelper","Use refuse Permission ACCESS_BACKGROUND_LOCATION")
            AppCompatActivity().genPermissionDialog(
                "提示信息",
                "当前应用缺少必要的权限，该功能暂时无法使用。如若需要，请单击【去开启】按钮前往设置中心进行权限授权。",
                rightText = { appSettingLauncher.launch(null) })?.show(AppCompatActivity().supportFragmentManager, "sfkajl")
        }
    }



    /**
     * 跳转应用设置页的Launcher
     * */
    fun ActivityResultCaller.appSettingsLauncher() =
        registerForActivityResult(LaunchAppSettingsContract()) {}

    /**
     * 跳转设置页的协定
     * */
    class LaunchAppSettingsContract : ActivityResultContract<Unit, Unit>() {
        override fun createIntent(context: Context, input: Unit?) =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.packageName, null))

        override fun parseResult(resultCode: Int, intent: Intent?) = Unit
    }

    class RequestPermissionContract: ActivityResultContract<String, Pair<String, Boolean>>(){
        private lateinit var mPermission: String

        override fun createIntent(context: Context, input: String): Intent {
            mPermission = input
            return Intent(ActivityResultContracts.RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS).putExtra(
                ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS, arrayOf(input)
            )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Pair<String, Boolean> {
            if (intent == null || resultCode != Activity.RESULT_OK) return mPermission to false
            val grantResults =
                intent.getIntArrayExtra(ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)
            return mPermission to
                    if (grantResults == null || grantResults.isEmpty()) false
                    else grantResults[0] == PackageManager.PERMISSION_GRANTED
        }

        override fun getSynchronousResult(
            context: Context,
            input: String?
        ): SynchronousResult<Pair<String, Boolean>>? =
            when {
                null == input -> SynchronousResult("" to false)
                ContextCompat.checkSelfPermission(context, input) == PackageManager.PERMISSION_GRANTED -> {
                    SynchronousResult(input to true)
                }
                else -> null
            }

    }


}


