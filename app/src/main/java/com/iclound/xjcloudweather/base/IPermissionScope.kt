package com.iclound.xjcloudweather.base

/**
 * 申请权限跳转接口
 */
interface IPermissionScope {
    //应用设置页面
    fun appSetting()

    //当前申请页面
    fun currentApply()
}