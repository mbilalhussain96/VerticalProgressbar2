package com.android.stepview




import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.stepview.bean.StepBean



/**
 * 日期：16/6/22 15:47
 *
 *
 * 描述：StepView
 */
class HorizontalStepView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    HorizontalStepsViewIndicator.OnDrawIndicatorListener {
    private var mTextContainer: RelativeLayout? = null
    private var mStepsViewIndicator: HorizontalStepsViewIndicator? = null
    private var mStepBeanList: List<StepBean>? = null
    private val mComplectingPosition = 0
    private var mUnComplectedTextColor =
        ContextCompat.getColor(getContext(), R.color.uncompleted_text_color) //定义默认未完成文字的颜色;
    private var mComplectedTextColor =
        ContextCompat.getColor(getContext(), android.R.color.white) //定义默认完成文字的颜色;
    private var mTextSize = 14 //default textSize
    private var mTextView: TextView? = null

    init {
        init()
    }

    private fun init() {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.widget_horizontal_stepsview, this)
        mStepsViewIndicator =
            rootView.findViewById<View>(R.id.steps_indicator) as HorizontalStepsViewIndicator
        mStepsViewIndicator!!.setOnDrawListener(this)
        mTextContainer = rootView.findViewById<View>(R.id.rl_text_container) as RelativeLayout
    }

    /**
     * 设置显示的文字
     *
     * @param stepsBeanList
     * @return
     */
    fun setStepViewTexts(stepsBeanList: List<StepBean>?): HorizontalStepView {
        mStepBeanList = stepsBeanList
        mStepsViewIndicator!!.setStepNum(mStepBeanList)
        return this
    }

    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    fun setStepViewUnComplectedTextColor(unComplectedTextColor: Int): HorizontalStepView {
        mUnComplectedTextColor = unComplectedTextColor
        return this
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    fun setStepViewComplectedTextColor(complectedTextColor: Int): HorizontalStepView {
        mComplectedTextColor = complectedTextColor
        return this
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    fun setStepsViewIndicatorUnCompletedLineColor(unCompletedLineColor: Int): HorizontalStepView {
        mStepsViewIndicator!!.setUnCompletedLineColor(unCompletedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    fun setStepsViewIndicatorCompletedLineColor(completedLineColor: Int): HorizontalStepView {
        mStepsViewIndicator!!.setCompletedLineColor(completedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    fun setStepsViewIndicatorDefaultIcon(defaultIcon: Drawable?): HorizontalStepView {
        mStepsViewIndicator!!.setDefaultIcon(defaultIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    fun setStepsViewIndicatorCompleteIcon(completeIcon: Drawable?): HorizontalStepView {
        mStepsViewIndicator!!.setCompleteIcon(completeIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setStepsViewIndicatorAttentionIcon(attentionIcon: Drawable?): HorizontalStepView {
        mStepsViewIndicator!!.setAttentionIcon(attentionIcon)
        return this
    }

    /**
     * set textSize
     *
     * @param textSize
     * @return
     */
    fun setTextSize(textSize: Int): HorizontalStepView {
        if (textSize > 0) {
            mTextSize = textSize
        }
        return this
    }

    override fun ondrawIndicator() {
        if (mTextContainer != null) {
            mTextContainer!!.removeAllViews()
            val complectedXPosition = mStepsViewIndicator!!.circleCenterPointPositionList
            if (mStepBeanList != null && complectedXPosition != null && complectedXPosition.size > 0) {
                for (i in mStepBeanList!!.indices) {
                    mTextView = TextView(context)
                    mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize.toFloat())
                    mTextView!!.text = mStepBeanList!![i].name
                    val spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    mTextView!!.measure(spec, spec)
                    // getMeasuredWidth
                    val measuredWidth = mTextView!!.measuredWidth
                    mTextView!!.x = complectedXPosition.get(i) - measuredWidth / 2
                    mTextView!!.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    if (i <= mComplectingPosition) {
                        mTextView!!.setTypeface(null, Typeface.BOLD)
                        mTextView!!.setTextColor(mComplectedTextColor)
                    } else {
                        mTextView!!.setTextColor(mUnComplectedTextColor)
                    }
                    mTextContainer!!.addView(mTextView)
                }
            }
        }
    }
}