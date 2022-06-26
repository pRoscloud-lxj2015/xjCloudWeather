package com.iclound.xjcloudweather.widgets

import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.Log
import kotlinx.coroutines.*

/**
 * create by shiju.wang
 * cloud
 */
class SunLoadingDrawable(val sun: Drawable, val cloud: Drawable) : Drawable(), Animatable {

    private var scope: CoroutineScope? = null

    private var centerWidth = 0

    var currentAngel = 0.1f

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        centerWidth = (bounds.right - bounds.left) * 2 / 5

        sun.setBounds(
            -centerWidth, -centerWidth, centerWidth, centerWidth
        )
        cloud.setBounds(
            -centerWidth, -centerWidth*2/3, centerWidth, centerWidth*3/2
        )
    }

    private fun startAnim() {
        if (scope == null) {
            scope = CoroutineScope(Job() + Dispatchers.Main)
            scope?.launch {
                while (isActive) {
                    withContext(Dispatchers.Default) {
                        delay(20)
                    }
                    updatePosition()
                }
            }
        }
    }
    var testValue = 0.0f
    private fun updatePosition() {

        currentAngel += 4.0f
        testValue += 4.0f
        if (currentAngel > 360f || testValue > 360f) {
            currentAngel = 0.0f
            testValue = 0.0f
        }
        //Log.e("updatePosition()", " :" + currentAngel + " :" + testValue)
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        canvas.translate(centerWidth.toFloat(), centerWidth.toFloat())
        canvas.save()
        canvas.rotate(currentAngel)

        sun.draw(canvas)

        canvas.restore()

        cloud.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setProgressRotation(rotation: Float) {
        currentAngel = rotation * 360
        invalidateSelf()
    }

    override fun start() {
        startAnim()
    }

    override fun stop() {
        scope?.cancel()
        scope = null
    }

    override fun isRunning(): Boolean {
        scope?.let {
            return it.isActive
        } ?: run {
            return false
        }
    }
}