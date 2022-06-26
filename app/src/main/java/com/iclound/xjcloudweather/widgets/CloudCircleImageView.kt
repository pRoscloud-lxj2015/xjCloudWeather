package com.iclound.xjcloudweather.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatImageView

class CloudCircleImageView : AppCompatImageView {
    private var mListener: Animation.AnimationListener? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    fun setAnimationListener(listener: Animation.AnimationListener?) {
        mListener = listener
    }

    public override fun onAnimationStart() {
        super.onAnimationStart()
        if (mListener != null) {
            mListener!!.onAnimationStart(animation)
        }
    }

    public override fun onAnimationEnd() {
        super.onAnimationEnd()
        if (mListener != null) {
            mListener!!.onAnimationEnd(animation)
        }
    }
}