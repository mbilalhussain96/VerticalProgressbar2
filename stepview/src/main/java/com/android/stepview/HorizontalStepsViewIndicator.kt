package com.android.stepview




import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.android.stepview.bean.StepBean


/**
 * 日期：16/6/22 14:15
 *
 *
 * 描述：StepsViewIndicator 指示器
 */
class HorizontalStepsViewIndicator @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    //定义默认的高度   definition default height
    private val defaultStepIndicatorNum =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, resources.displayMetrics)
            .toInt()
    private var mCompletedLineHeight //完成线的高度     definition completed line height
            = 0f

    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    var circleRadius //圆的半径  definition circle radius
            = 0f
        private set
    private var mCompleteIcon //完成的默认图片    definition default completed icon
            : Drawable? = null
    private var mAttentionIcon //正在进行的默认图片     definition default underway icon
            : Drawable? = null
    private var mDefaultIcon //默认的背景图  definition default unCompleted icon
            : Drawable? = null
    private var mCenterY //该view的Y轴中间位置     definition view centerY position
            = 0f
    private var mLeftY //左上方的Y位置  definition rectangle LeftY position
            = 0f
    private var mRightY //右下方的位置  definition rectangle RightY position
            = 0f
    private var mStepBeanList //当前有几部流程    there are currently few step
            : List<StepBean>? = null
    private var mStepNum = 0
    private var mLinePadding //两条连线之间的间距  definition the spacing between the two circles
            = 0f
    private var mCircleCenterPointPositionList //定义所有圆的圆心点位置的集合 definition all of circles center point list
            : MutableList<Float>? = null
    private var mUnCompletedPaint //未完成Paint  definition mUnCompletedPaint
            : Paint? = null
    private var mCompletedPaint //完成paint      definition mCompletedPaint
            : Paint? = null
    private var mUnCompletedLineColor =
        ContextCompat.getColor(getContext(), R.color.uncompleted_color) //定义默认未完成线的颜色  definition
    private var mCompletedLineColor = Color.WHITE //定义默认完成线的颜色      definition mCompletedLineColor
    private var mEffects: PathEffect? = null
    private var mComplectingPosition //正在进行position   underway position
            = 0
    private var mPath: Path? = null
    private var mOnDrawListener: OnDrawIndicatorListener? = null
    private var screenWidth //this screen width
            = 0

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    fun setOnDrawListener(onDrawListener: OnDrawIndicatorListener?) {
        mOnDrawListener = onDrawListener
    }

    init {
        init()
    }

    /**
     * init
     */
    private fun init() {
        mStepBeanList = ArrayList()
        mPath = Path()
        mEffects = DashPathEffect(floatArrayOf(8f, 8f, 8f, 8f), 1f)
        mCircleCenterPointPositionList = ArrayList() //初始化
        mUnCompletedPaint = Paint()
        mCompletedPaint = Paint()
        mUnCompletedPaint!!.isAntiAlias = true
        mUnCompletedPaint!!.color = mUnCompletedLineColor
        mUnCompletedPaint!!.style = Paint.Style.STROKE
        mUnCompletedPaint!!.strokeWidth = 2f
        mCompletedPaint!!.isAntiAlias = true
        mCompletedPaint!!.color = mCompletedLineColor
        mCompletedPaint!!.style = Paint.Style.STROKE
        mCompletedPaint!!.strokeWidth = 2f
        mUnCompletedPaint!!.pathEffect = mEffects
        mCompletedPaint!!.style = Paint.Style.FILL

        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum
        //圆的半径  set mCircleRadius
        circleRadius = 0.28f * defaultStepIndicatorNum
        //线与线之间的间距    set mLinePadding
        mLinePadding = 0.85f * defaultStepIndicatorNum
        mCompleteIcon = ContextCompat.getDrawable(context, R.drawable.complted) //已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(context, R.drawable.attention) //正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(context, R.drawable.default_icon) //未完成的icon
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = defaultStepIndicatorNum * 2
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            screenWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        var height = defaultStepIndicatorNum
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec))
        }
        width = (mStepNum * circleRadius * 2 - (mStepNum - 1) * mLinePadding).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //获取中间的高度,目的是为了让该view绘制的线和圆在该view垂直居中   get view centerY，keep current stepview center vertical
        mCenterY = 0.5f * height
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - mCompletedLineHeight / 2
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2
        mCircleCenterPointPositionList!!.clear()
        for (i in 0 until mStepNum) {
            //先计算全部最左边的padding值（getWidth()-（圆形直径+两圆之间距离）*2）
            val paddingLeft =
                screenWidth - mStepNum * circleRadius * 2 - (mStepNum - 1) * mLinePadding / 2
            //add to list
            mCircleCenterPointPositionList!!.add(paddingLeft + circleRadius + i * circleRadius * 2 + i * mLinePadding)
        }
        /**
         * set listener
         */
        if (mOnDrawListener != null) {
            mOnDrawListener!!.ondrawIndicator()
        }
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mOnDrawListener != null) {
            mOnDrawListener!!.ondrawIndicator()
        }
        mUnCompletedPaint!!.color = mUnCompletedLineColor
        mCompletedPaint!!.color = mCompletedLineColor

        //-----------------------画线-------draw line-----------------------------------------------
        for (i in 0 until mCircleCenterPointPositionList!!.size - 1) {
            //前一个ComplectedXPosition
            val preComplectedXPosition = mCircleCenterPointPositionList!![i]
            //后一个ComplectedXPosition
            val afterComplectedXPosition = mCircleCenterPointPositionList!![i + 1]
            if (i <= mComplectingPosition && mStepBeanList!![0].state != StepBean.STEP_UNDO) //判断在完成之前的所有点
            {
                //判断在完成之前的所有点，画完成的线，这里是矩形,很细的矩形，类似线，为了做区分，好看些
                canvas.drawRect(
                    preComplectedXPosition + circleRadius - 10,
                    mLeftY,
                    afterComplectedXPosition - circleRadius + 10,
                    mRightY,
                    mCompletedPaint!!
                )
            } else {
                mPath!!.moveTo(preComplectedXPosition + circleRadius, mCenterY)
                mPath!!.lineTo(afterComplectedXPosition - circleRadius, mCenterY)
                canvas.drawPath(mPath!!, mUnCompletedPaint!!)
            }
        }
        //-----------------------画线-------draw line-----------------------------------------------


        //-----------------------画图标-----draw icon-----------------------------------------------
        for (i in mCircleCenterPointPositionList!!.indices) {
            val currentComplectedXPosition = mCircleCenterPointPositionList!![i]
            val rect = Rect(
                (currentComplectedXPosition - circleRadius).toInt(),
                (mCenterY - circleRadius).toInt(),
                (currentComplectedXPosition + circleRadius).toInt(),
                (mCenterY + circleRadius).toInt()
            )
            val stepsBean = mStepBeanList!![i]
            if (stepsBean.state == StepBean.STEP_UNDO) {
                mDefaultIcon!!.bounds = rect
                mDefaultIcon!!.draw(canvas)
            } else if (stepsBean.state == StepBean.STEP_CURRENT) {
                mCompletedPaint!!.color = Color.WHITE
                canvas.drawCircle(
                    currentComplectedXPosition,
                    mCenterY,
                    circleRadius * 1.1f,
                    mCompletedPaint!!
                )
                mAttentionIcon!!.bounds = rect
                mAttentionIcon!!.draw(canvas)
            } else if (stepsBean.state == StepBean.STEP_COMPLETED) {
                mCompleteIcon!!.bounds = rect
                mCompleteIcon!!.draw(canvas)
            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------
    }

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    val circleCenterPointPositionList: List<Float>?
        get() = mCircleCenterPointPositionList

    /**
     * 设置流程步数
     *
     * @param stepsBeanList 流程步数
     */
    fun setStepNum(stepsBeanList: List<StepBean>?) {
        mStepBeanList = stepsBeanList
        mStepNum = mStepBeanList!!.size
        if (mStepBeanList != null && mStepBeanList!!.size > 0) {
            for (i in 0 until mStepNum) {
                val stepsBean = mStepBeanList!![i]
                run {
                    if (stepsBean.state == StepBean.STEP_COMPLETED) {
                        mComplectingPosition = i
                    }
                }
            }
        }
        requestLayout()
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    fun setUnCompletedLineColor(unCompletedLineColor: Int) {
        mUnCompletedLineColor = unCompletedLineColor
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    fun setCompletedLineColor(completedLineColor: Int) {
        mCompletedLineColor = completedLineColor
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    fun setDefaultIcon(defaultIcon: Drawable?) {
        mDefaultIcon = defaultIcon
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    fun setCompleteIcon(completeIcon: Drawable?) {
        mCompleteIcon = completeIcon
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setAttentionIcon(attentionIcon: Drawable?) {
        mAttentionIcon = attentionIcon
    }

    /**
     * 设置对view监听
     */
    interface OnDrawIndicatorListener {
        fun ondrawIndicator()
    }
}