package com.iclound.xjcloudweather.widgets

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.iclound.xjcloudweather.R
import com.iclound.xjcloudweather.adapter.CityManagerAdapter
import com.iclound.xjcloudweather.app.App
import com.iclound.xjcloudweather.base.IDragSwipListener
import com.iclound.xjcloudweather.base.onItemDragSwipListener
import com.iclound.xjcloudweather.utils.DisplayUtils
import java.lang.Math.abs
import java.util.*


class DragSwipeItemTouchCallback(private val cityManagerAdapter: CityManagerAdapter) : ItemTouchHelper.Callback(),
    IDragSwipListener {
    private val TAG = "DragSwipeItemTouchCallback"
    private var mDefaultScrollX = 0
    private var mMarginValue = 0
    private var mCurrentScrollX = 0
    private var mCurrentScrollXWhenInactive = 0
    private var mInitXWhenInactive = 0f
    private var mFirstInactive = false
    private var screenWidth = 0
    private var itemMarginRight = 0
    private var viewX = 0f
    private var moveView: View? = null
    private var noMoveView: View?  = null

    var swipItemView: View? = null

    protected var mOnItemDragSwipEndListener: onItemDragSwipListener? = null

    init{
        screenWidth = Resources.getSystem().displayMetrics.widthPixels
        mMarginValue = DisplayUtils.dp2px(15f)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Int.MAX_VALUE.toFloat()
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //拖动
        val dragFlags = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return if(isViewCreateByAdapter(viewHolder))
            makeMovementFlags(0, 0)
        else  makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    /**
     * 选择模式
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        Log.d(TAG, "select:" + actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && !isViewCreateByAdapter(viewHolder!!)) {
            val vibrator = App.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(12, -1))
            }else{
                vibrator.vibrate(12)
            }
            viewHolder.itemView?.scaleX = 1.02f
            viewHolder.itemView?.scaleY = 1.02f
            viewHolder.itemView.setTag(R.id.adpater_item_drag, true)
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder!!)) {
            moveView =  viewHolder.itemView.findViewById<LinearLayout>(R.id.ic_delete)
            noMoveView = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.citymanage_item_layout)
            mDefaultScrollX = viewHolder?.itemView?.findViewById<LinearLayout>(R.id.ic_delete)?.width!!
            itemMarginRight = screenWidth - viewHolder?.itemView?.right
            mFirstInactive = true
            viewHolder.itemView.setTag(R.id.adpater_item_swip, true)
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onMoved(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y)
        val from = getViewHolderPosition(source)
        val to = getViewHolderPosition(target)
        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (i in from until to) {
                    Collections.swap(cityManagerAdapter.data, i, i + 1)
                }
            } else {
                for(i in from downTo to + 1){
                    Collections.swap(cityManagerAdapter.data, i, i - 1)
                }
            }
            cityManagerAdapter.notifyItemMoved(source.adapterPosition, target.adapterPosition)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder)){ //侧滑
            val itemView = viewHolder.itemView
            if(dX == 0f){
                mCurrentScrollX = viewHolder.itemView.scrollX
                mFirstInactive = true
                viewX = moveView?.x!!
               // moveView.left = viewX.toInt()
            }
            if(isCurrentlyActive){
                viewHolder.itemView.scrollTo(mCurrentScrollX + -dX.toInt(), 0)
                val loc = IntArray(2)
                moveView?.getLocationOnScreen(loc)
                val value = dX / mDefaultScrollX
                var itemViewScale = 1.0f

                if(dX <= 0 ) {
                    var alpha = abs(value)
                    if(alpha > 1.0f) alpha = 1f
                    //if(loc[0] > screenWidth - mDefaultScrollX - itemMarginRight){
                    val distanse = screenWidth - itemMarginRight* 2
                    var scrollLeft = distanse - mDefaultScrollX - dX
                    if(scrollLeft >= distanse){
                        scrollLeft = distanse.toFloat()
                    }
                    if(viewHolder.itemView.scrollX >= mDefaultScrollX){ //滚动距离超过delete的宽度，不设置
                        scrollLeft = distanse.toFloat()
                    }else{
                        moveView?.setX(scrollLeft)
                        moveView?.alpha = alpha
                        moveView?.scaleY = alpha
                        moveView?.scaleX = alpha
                    }
                    if(1 - (value) > 0.97f){
                        itemViewScale = 0.97F
                    } else{
                        itemViewScale = 1 - (value)
                    }
                    noMoveView?.scaleX = itemViewScale
                    noMoveView?.scaleY = itemViewScale
                    // moveView.left = screenWidth - mDefaultScrollX - itemMarginRight*2
                    //Log.d(TAG,"tag: 左" + " " + moveView!!.left + " translationX:" + viewHolder.itemView.translationX+ " dX:" + dX + " scrollLeft:" +  scrollLeft)
                }else{
                    var alpha = 1 - abs(value)
                    if( alpha < 0.0f) alpha = 0f
                    moveView?.setX((screenWidth - itemMarginRight*2 - dX))
                    moveView?.alpha = alpha
                    moveView?.scaleY =alpha
                    moveView?.scaleX =alpha
                    ///Log.d(TAG,"tag: 右" + " " + moveView!!.left + " translationX:" + viewHolder.itemView.translationX+ " dX:" + dX + " value:" + alpha)
                }
                Log.d(TAG, " " + moveView!!.x + " scrollX:" + noMoveView?.scrollX+ " dX:" + dX + " x:" +  moveView!!.scrollX)
            }else{
                if(mFirstInactive){
                    mFirstInactive = false
                    mCurrentScrollXWhenInactive = viewHolder.itemView.scrollX
                    mInitXWhenInactive = dX
                }
                if(viewHolder.itemView.scrollX >= mDefaultScrollX / 2){
                    moveView?.x = viewX
                    viewHolder.itemView.scrollTo(Math.max(mCurrentScrollX + (-dX).toInt(), mDefaultScrollX), 0)
                }else{
                    moveView?.x = viewX
                    viewHolder.itemView.scrollTo((mCurrentScrollXWhenInactive * dX / mInitXWhenInactive).toInt(), 0)
                }
            }
        }else{
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        //Log.d(TAG,"tag-clearView:" + viewHolder.itemView.getTag(R.id.adpater_item_swip))
        if (isViewCreateByAdapter(viewHolder)) {
            return
        }
        viewHolder.itemView?.scaleX = 1.0f
        viewHolder.itemView?.scaleY = 1.0f
        if ( viewHolder.itemView.getTag(R.id.adpater_item_drag) != null
            && viewHolder.itemView.getTag(R.id.adpater_item_drag) as Boolean) {
            //拖拽结束
            //cityManagerAdapter.onSort?.invoke(cityManagerAdapter.data)
            viewHolder.itemView.setTag(R.id.adpater_item_drag, false)
            viewHolder.itemView?.scaleX = 1f
            viewHolder.itemView?.scaleY = 1f
            mOnItemDragSwipEndListener?.onItemDragEnd(cityManagerAdapter.data)

        }
        //侧滑
        if(viewHolder.itemView.getTag(R.id.adpater_item_swip) != null &&
            viewHolder.itemView.getTag(R.id.adpater_item_swip) as Boolean ){
            //tag 恢复
            viewHolder.itemView.setTag(R.id.adpater_item_swip, false)
            // Log.d(TAG,"tag:-clearView:" + " x:" + moveView!!.left +  " translationX:" + viewHolder.itemView.translationX + " viewX:" + viewX)
            //itemView 恢复
            if(viewHolder.itemView.scrollX >= mDefaultScrollX){
                val vibrator = App.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(20, -1))
                }else{
                    vibrator.vibrate(20)
                }
                viewHolder.itemView.scrollTo(mDefaultScrollX, 0)

            }else if(viewHolder.itemView.scrollX < 0){
                viewHolder.itemView.scrollTo(0, 0)
            }
            mFirstInactive = false
            swipItemView = viewHolder.itemView
            moveView?.alpha = 1f
            moveView?.scaleY = 1f
            moveView?.scaleX = 1f
            noMoveView?.scaleX = 1f
            noMoveView?.scaleY = 1f
            //Log.d(TAG,"tag:-clearView:" + " x:" + viewHolder.itemView.scrollX +  " translationX:" + viewHolder.itemView.translationX + " viewX:" + viewX)
        }
    }

    /**
     * 拖拽模式move
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return viewHolder.itemViewType == target.itemViewType
        //return
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //TODO("Not yet implemented")
    }

    private fun inRange(position: Int): Boolean {
        return position >= 0 && position < cityManagerAdapter.data.size
    }

    protected fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder): Int {
        return viewHolder.adapterPosition - cityManagerAdapter.headerLayoutCount
    }

    private fun isViewCreateByAdapter(viewHolder: RecyclerView.ViewHolder): Boolean {
        val type = viewHolder.itemViewType
       // val position = getViewHolderPosition(viewHolder)
        //println("tag-isViewCreateBy"+ " :" + type)
        return type == BaseQuickAdapter.HEADER_VIEW
    }

    override fun setOnItemDragSwipListener(onItemDragSwipListener: onItemDragSwipListener) {
        this.mOnItemDragSwipEndListener = onItemDragSwipListener
    }
//
//    private fun findMaxElevation(recyclerView: RecyclerView, itemView: View): Float {
//        val childCount = recyclerView.childCount
//        var max = 0f
//        for (i in 0 until childCount) {
//            val child = recyclerView.getChildAt(i)
//            if (child === itemView) {
//                continue
//            }
//            val elevation = ViewCompat.getElevation(child)
//            if (elevation > max) {
//                max = elevation
//            }
//        }
//        return max
//    }
//
//    private fun setElevation(recyclerView: RecyclerView, viewHolder: View, isCurrentlyActive: Boolean){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (isCurrentlyActive) {
//                var originalElevation = noMoveView?.getTag(R.id.adpater_item_elevation)!!
//                if (originalElevation == 1) {
//                    originalElevation = ViewCompat.getElevation(noMoveView!!)
//                    val newElevation = 1f + 5f +findMaxElevation(recyclerView, noMoveView!!)
//                    println(" new:" + newElevation)
//                    ViewCompat.setElevation(noMoveView!!, newElevation)
//                    noMoveView?.setTag(R.id.adpater_item_elevation, originalElevation)
//                }
//            }
//        }
//    }
//
//    private fun resetElevation(viewHolder: View?){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val tag: Any = noMoveView!!.getTag(R.id.adpater_item_elevation)
//            if (tag is Float) {
//                ViewCompat.setElevation(noMoveView!!, (tag as Float))
//            }
//            noMoveView!!.setTag(R.id.adpater_item_elevation, null)
//        }
//    }

}