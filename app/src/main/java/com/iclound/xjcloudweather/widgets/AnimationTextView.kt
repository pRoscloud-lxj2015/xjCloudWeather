package com.iclound.xjcloudweather.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import com.iclound.xjcloudweather.base.Daily
import com.iclound.xjcloudweather.utils.CharacterUtils
import com.iclound.xjcloudweather.widgets.bean.CharacterDiffResult


class AnimationTextView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val TAG = "AnimationTextView"
    private var mPaint: Paint
    private var mOldPaint: Paint

    private var mTextSize: Int = 0
    private var mText: CharSequence
    private var mOldText: CharSequence

    private var differentList: ArrayList<CharacterDiffResult> = ArrayList<CharacterDiffResult>()
    private val gaps = arrayOfNulls<Float>(50)
    private val oldGaps = arrayOfNulls<Float>(50)

    private var oldStartX = 0f
    private var startX = 0f
    private var startY = 0f

    private val charTime = 400
    private val mostCount = 20
    private var duration = 0L
    private var progress = 1f

    init{
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = this.textColors.defaultColor
        mPaint.style = Paint.Style.FILL

        mOldPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOldPaint.color = this.textColors.defaultColor
        mOldPaint.style = Paint.Style.FILL

        mText = this.text.toString()
        mOldText = this.text

        mTextSize = this.textSize.toInt()
        this.ellipsize = TextUtils.TruncateAt.END
        this.postDelayed({
            prepareAnimate() //动画
        }, 50)

        //textAnimationStart(this.text.toString())

    }

    private fun prepareAnimate(){
        mTextSize = textSize.toInt()
        mPaint.textSize = mTextSize.toFloat()

        var lenth: Int = mText.length
        for (i in 0 until lenth) {
            Log.d(TAG,"text字体：" + mText[i].toString() + " leng:" + lenth)
            gaps[i] = mPaint.measureText(mText[i].toString() + "")
        }
        mOldPaint.textSize = mTextSize.toFloat()
        lenth = mOldText.length
        for (i in 0 until lenth) {
            Log.d(TAG,"old字体：" + mText[i].toString() + " leng:" + lenth)
            oldGaps[i] = mOldPaint.measureText(mOldText[i].toString() + "")
        }

        oldStartX = measuredWidth - compoundPaddingLeft - paddingLeft - mOldPaint.measureText(mOldText as String?)///2f
        startX = measuredWidth - compoundPaddingLeft - paddingLeft - mPaint.measureText(mText.toString())///2f
        startY = baseline.toFloat()
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(mOldText, mText))

    }

    private fun animateStart(){
        val count = if(mText.length <= 0) {
            1
        } else{
            mText.length
        }
        duration = (charTime + (charTime / mostCount) * (count - 1)).toLong()
        Log.d(TAG,"start:" + " duration:" + duration)
        val valueAnimator = ValueAnimator.ofFloat(0f, duration.toFloat()).setDuration(duration)
        valueAnimator.run {
            interpolator = AccelerateInterpolator()
            addUpdateListener(object: ValueAnimator.AnimatorUpdateListener{
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    progress = animation.animatedValue as Float
                    //invalidate()
                    postInvalidate()
                }

            })
        }
        valueAnimator.start()
    }

    fun textAnimationStart(text: String= ""){
        this.text = text
        mOldText = mText
        mText = text
        prepareAnimate()
        animateStart()
    }

    override fun onDraw(canvas: Canvas?) {
        var offset = startX
        var oldOffset = oldStartX
        //val maxLength: Int? = mOldText?.length?.let { Math.max(it, mOldText?.length!!) }

        val maxLength = Math.max(mText.length, mOldText.length)

        for (i in 0 until maxLength) {
            // draw old text
            if (i < mOldText.length) {
                val percent = progress / duration
                val move: Int = CharacterUtils.needMove(i, differentList)
                if (move != -1) {
                    mOldPaint.textSize = mTextSize.toFloat()
                    mOldPaint.alpha = 255
                    var p = percent * 2f
                    p = if (p > 1) 1F else p
                    val distX: Float = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps)
                    Log.d(TAG,"old Text:" + mText[i] + " distX:" + p + " percent:" + percent)
                    canvas?.drawText(mOldText[i] + "", 0, 1, distX, startY, mOldPaint)
                } else {
                    mOldPaint.alpha = ((1 - percent) * 255).toInt()
                    mOldPaint.textSize = mTextSize * (1 - percent)
                    val width = mOldPaint.measureText(mOldText[i] + "")

                    canvas?.drawText(mOldText[i]+ "", 0, 1,
                        oldOffset + (oldGaps[i]!! - width) / 2, startY, mOldPaint)
                }
                oldOffset += oldGaps[i]!!
            }

            // draw new text
            if (i < mText.length) {
                if (!CharacterUtils.stayHere(i, differentList)) {
                    var alpha = (255f / charTime * (progress - charTime * i / mostCount)).toInt()
                    if (alpha > 255) alpha = 255
                    if (alpha < 0) alpha = 0
                    var size = mTextSize * 1f / charTime * (progress - charTime * i / mostCount)
                    if (size > mTextSize) size = mTextSize.toFloat()
                    if (size < 0) size = 0f
                    mPaint.alpha = alpha
                    mPaint.textSize = size
                    val width = mPaint.measureText(mText[i] + "")
                    Log.d("WarningDetail" ,"new Text:" + mText[i] + " width:" + width)
                    canvas?.drawText(mText[i] + "", 0, 1,
                        offset + (gaps[i]!! - width) / 2, startY, mPaint)
                }
                offset += gaps[i]!!
            }
        }
       // super.onDraw(canvas)
    }


}