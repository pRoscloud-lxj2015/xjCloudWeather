package com.iclound.xjcloudweather.utils

import androidx.appcompat.app.AppCompatActivity
import com.iclound.xjcloudweather.widgets.PermissionDialog


fun AppCompatActivity.genPermissionDialog(
    title: String,
    content: String,
    rightText: (() -> Unit)? = null,
    cancel: (() -> Unit)? = null,
): PermissionDialog? = PermissionDialog.newBuilder().apply{
    title.let { setTitle(it) }
    content.let { setContent(it) }
    cancel?.let { setCancelAble(false)}
    rightText?.let { setRightText("去开启")}
}.build()
