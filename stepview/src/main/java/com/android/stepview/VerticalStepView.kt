package com.android.stepview



import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/**
 * 日期：16/6/24 11:48
 *
 *
 * 描述：
 */
@RequiresApi(Build.VERSION_CODES.M)
class VerticalStepView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), VerticalStepViewIndicator.OnDrawIndicatorListener {
    private var mTextContainer: RelativeLayout? = null
    private var mStepsViewIndicator: VerticalStepViewIndicator? = null
    private var  mCurrentStateNumber=0

    private var mTexts: List<String>? = null
    private var mComplectingPosition = 0
    private var mUnComplectedTextColor =
        ContextCompat.getColor(getContext(), R.color.uncompleted_text_color) //定义默认未完成文字的颜色;
    private var mComplectedTextColor =
        ContextCompat.getColor(getContext(), R.color.completed_text_color) //定义默认完成文字的颜色;
    private var mTextSize = 20 //default textSize
    private var mTextView: TextView? = null

    init {
        init()
    }

    private fun init() {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.widget_vertical_stepsview, this)
        mStepsViewIndicator =
            rootView.findViewById<View>(R.id.steps_indicator) as VerticalStepViewIndicator
        mStepsViewIndicator!!.setOnDrawListener(this)
        mTextContainer = rootView.findViewById<View>(R.id.rl_text_container) as RelativeLayout
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 设置显示的文字
     *
     * @param texts
     * @return
     */
    fun setStepViewTexts(texts: List<String>?): VerticalStepView {
        mTexts = texts
        if (texts != null) {
            mStepsViewIndicator!!.setStepNum(mTexts!!.size)
        } else {
            mStepsViewIndicator!!.setStepNum(0)
        }
        return this
    }

    /**
     * 设置正在进行的position
     *
     * @param complectingPosition
     * @return
     */
    fun setStepsViewIndicatorComplectingPosition(complectingPosition: Int): VerticalStepView {
        mComplectingPosition = complectingPosition
        mStepsViewIndicator!!.setComplectingPosition(complectingPosition)
        return this
    }

    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    fun setStepViewUnComplectedTextColor(unComplectedTextColor: Int): VerticalStepView {
        mUnComplectedTextColor = unComplectedTextColor
        return this
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    fun setStepViewComplectedTextColor(complectedTextColor: Int): VerticalStepView {
        mComplectedTextColor = complectedTextColor
        return this
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    fun setStepsViewIndicatorUnCompletedLineColor(unCompletedLineColor: Int): VerticalStepView {
        mStepsViewIndicator!!.setUnCompletedLineColor(unCompletedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    fun setStepsViewIndicatorCompletedLineColor(completedLineColor: Int): VerticalStepView {
        mStepsViewIndicator!!.setCompletedLineColor(completedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    fun setStepsViewIndicatorDefaultIcon(defaultIcon: Drawable?): VerticalStepView {
        mStepsViewIndicator!!.setDefaultIcon(defaultIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    fun setStepsViewIndicatorCompleteIcon(completeIcon: Drawable?): VerticalStepView {
        mStepsViewIndicator!!.setCompleteIcon(completeIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setStepsViewIndicatorAttentionIcon(attentionIcon: Drawable?): VerticalStepView {
        mStepsViewIndicator!!.setAttentionIcon(attentionIcon)
        return this
    }

    /**
     * is reverse draw 是否倒序画
     *
     * @param isReverSe default is true
     * @return
     */
    fun reverseDraw(isReverSe: Boolean): VerticalStepView {
        mStepsViewIndicator!!.reverseDraw(isReverSe)
        return this
    }

    /**
     * set linePadding  proportion 设置线间距的比例系数
     *
     * @param linePaddingProportion
     * @return
     */
    fun setLinePaddingProportion(linePaddingProportion: Float): VerticalStepView {
        mStepsViewIndicator!!.setIndicatorLinePaddingProportion(linePaddingProportion)
        return this
    }

    /**
     * set textSize
     *
     * @param textSize
     * @return
     */
    fun setTextSize(textSize: Int): VerticalStepView {
        if (textSize > 0) {
            mTextSize = textSize
        }
        return this
    }

    override fun ondrawIndicator() {
        if (mTextContainer != null) {
            mTextContainer!!.removeAllViews() //clear ViewGroup
            val complectedXPosition = mStepsViewIndicator!!.circleCenterPointPositionList
            if (mTexts != null && complectedXPosition != null && complectedXPosition.size > 0) {
                for (i in mTexts!!.indices) {
                    mTextView = TextView(context)
                    mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize.toFloat())
                    mTextView!!.text = mTexts!![i]
//                    mTextView!!.y = complectedXPosition[i] - mStepsViewIndicator!!.circleRadius / 2
                    mTextView!!.y = complectedXPosition[i] - mStepsViewIndicator!!.circleRadius / 1
                    mTextView!!.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    if (i < mComplectingPosition) {
                        mTextView!!.setTypeface(null, Typeface.BOLD)
                        mTextView!!.setTextColor(mComplectedTextColor)
                    } else {
                        mTextView!!.setTypeface(null, Typeface.BOLD)
                        mTextView!!.setTextColor(mUnComplectedTextColor)
                    }
                    mTextContainer!!.addView(mTextView)
                }
            }
        }
    }
}