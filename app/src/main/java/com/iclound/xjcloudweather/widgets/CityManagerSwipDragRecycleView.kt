package com.iclound.xjcloudweather.widgets

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.NotNull
import kotlin.math.abs

class CityManagerSwipDragRecycleView : RecyclerView {

    private val INVALID_POSITION = -1
    private val INVALID_CHILD_WIDTH = -1 //子ItemView
    private val SNAP_VELOCITY = 600 //最小滑动速度

    private var mVelocityTracker: VelocityTracker? = null // 速度追踪器
    private var mTouchSlop  = 0 // 认为是滑动的最小距离（一般由系统提供）
    private var mTouchFrame: Rect? = null // 子View所在的矩形范围
    private var mScroller: Scroller? = null
    private var mLastX: Float? = 0f // 滑动过程中记录上次触碰点X

    private var mFirstX: Float?  = 0f
    private var mFirstY: Float?  = 0f // 首次触碰范围
    private var mIsSlide = false     // 是否滑动子View
    private var mFlingView: ViewGroup? = null // 触碰的子View
    private var mPosition = 0   // 触碰的view在可见item中的位置
    private var mMenuViewWidth  = 0   // 菜单按钮宽度

    private var stateCallback: StateCallback? = null
    private var isTouchOpened = false

    constructor(context: Context) : super(context, null) {}
    constructor(context: Context, @NotNull attrs: AttributeSet?) : super(context, attrs, 0) {}
    constructor(context: Context, @NotNull attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mScroller = Scroller(context)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        var x = e?.x
        var y = e?.y
        Log.e("dragEnable : false", ""+ e?.action)
        obtainVelocity(e)
        when(e?.action){
            MotionEvent.ACTION_DOWN -> {
                if(mScroller?.isFinished == true){
                    mScroller?.abortAnimation()
                }
                mFirstX =x
                mLastX = x
                mFirstY = y

                mPosition = x?.let { y?.let { it1 -> pointToPosition(it, it1) } }!!
                if(mPosition != INVALID_POSITION){
                    stateCallback?.dragEnable(false)
                    val view = mFlingView
                    mFlingView = getChildAt(mPosition) as ViewGroup
                    isTouchOpened = (mFlingView == view && (mFlingView?.scaleX != 0.0f))
                    if(view != null && mFlingView != null && view.scaleX != 0.0f){
                        view.scrollTo(0, 0)
                        return true
                    }
                    mMenuViewWidth = if(mFlingView?.childCount == 2){
                        mFlingView?.getChildAt(1)?.width!!
                    }else{
                        INVALID_CHILD_WIDTH
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.computeCurrentVelocity(1000)
                // 此处有俩判断，满足其一则认为是侧滑：
                // 1.如果x方向速度大于y方向速度，且大于最小速度限制；
                // 2.如果x方向的侧滑距离大于y方向滑动距离，且x方向达到最小滑动距离；
                val xVelocity = mVelocityTracker!!.xVelocity
                val yVelocity = mVelocityTracker!!.yVelocity
                if (Math.abs(xVelocity) > SNAP_VELOCITY && Math.abs(xVelocity) > Math.abs(yVelocity)
                    || abs(x!! - mFirstX!!) >= mTouchSlop
                    && abs(x - mFirstX!!) > abs(y!! - mFirstY!!)
                ) {
                    mIsSlide = true
                    stateCallback?.dragEnable(false)
                    return true
                } else {
                    if (!isTouchOpened) {
                        stateCallback?.dragEnable(true)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                releaseVelocity()
            }
            else ->{}
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (mIsSlide && mPosition != INVALID_POSITION) {
            var x = e?.x
            obtainVelocity(e)
            when(e?.action){
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE -> {
                    // 随手指滑动
                    // 随手指滑动
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH) {
                        val dx = x?.let { mLastX?.minus(it) }
                        if (dx?.let { mFlingView?.scrollX?.plus(it) }!! <= mMenuViewWidth
                            && mFlingView?.scrollX?.plus(dx)!! > 0) {
                            mFlingView!!.scrollBy(dx.toInt(), 0)
                        }
                        mLastX = x
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH) {
                        val scrollX = mFlingView?.scrollX
                        mVelocityTracker!!.computeCurrentVelocity(1000)
                        // 此处有两个原因决定是否打开菜单：
                        // 1.菜单被拉出宽度大于菜单宽度一半；
                        // 2.横向滑动速度大于最小滑动速度；
                        // 注意：之所以要小于负值，是因为向左滑则速度为负值
                        if (mVelocityTracker?.xVelocity!! < SNAP_VELOCITY) {    // 向左侧滑达到侧滑最低速度，则打开
                            val del = abs(mMenuViewWidth - scrollX!!)
                            val t = (del / mVelocityTracker?.xVelocity!! * 1000).toInt()
                            mScroller?.startScroll(scrollX, 0, mMenuViewWidth - scrollX, 0, Math.abs(t))
                        } else if (mVelocityTracker!!.xVelocity >= SNAP_VELOCITY) {  // 向右侧滑达到侧滑最低速度，则关闭
                            mScroller?.startScroll(scrollX!!, 0, -scrollX, 0, abs(scrollX))
                        } else if (scrollX!! >= mMenuViewWidth / 2) { // 如果超过删除按钮一半，则打开
                            mScroller?.startScroll(
                                scrollX,
                                0,
                                mMenuViewWidth - scrollX,
                                0,
                                Math.abs(mMenuViewWidth - scrollX)
                            )
                        } else {    // 其他情况则关闭
                            mScroller?.startScroll(scrollX, 0, -scrollX, 0, abs(scrollX))
                        }
                        invalidate()
                    }
                    mMenuViewWidth = INVALID_CHILD_WIDTH
                    mIsSlide = false
                    mPosition = INVALID_POSITION
                    releaseVelocity()
                }
            }
            return true
        }else{
            closeMenu()
            releaseVelocity()
        }
        return super.onTouchEvent(e)
    }

    override fun computeScroll() {
        if(mScroller?.computeScrollOffset() == true){
            mScroller?.currX?.let { mScroller?.currY?.let { it1 -> mFlingView?.scrollTo(it, it1) } }
            invalidate()
        }
    }

    fun closeMenu() {
        if (mFlingView != null && mFlingView?.scrollX != 0) {
            mFlingView?.scrollTo(0, 0)
        }
    }

    private fun pointToPosition(x: Float, y: Float): Int {
        var frame = mTouchFrame
        if (frame == null) {
            mTouchFrame = Rect()
            frame = mTouchFrame
        }

        val count = childCount
        for (i in count - 1 downTo 0) {
            val child = getChildAt(i)
            if (child.visibility == VISIBLE) {
                child.getHitRect(frame)
                if (frame?.contains(x.toInt(), y.toInt()) == true) {
                    return i
                }
            }
        }
        return INVALID_POSITION
    }

    private fun obtainVelocity(event: MotionEvent?) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }

    fun setStateCallback(callback: StateCallback?) {
        stateCallback = callback
    }

    private fun releaseVelocity() {
        mVelocityTracker?.clear()
        mVelocityTracker?.recycle()
        mVelocityTracker = null
    }

    interface StateCallback {
        fun dragEnable(enable: Boolean)
    }
}