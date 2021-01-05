
package com.ymx.driver.view.cardview;
import ShadowManager
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.ymx.driver.R

import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class LCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val defaultShadowSize = 12
    private val defaultShadowStartAlpha = 10
    private val defaultShadowColor = Color.parseColor("#05000000")
    private val defaultCardBackgroundColor = 99999999
    private var cardElevation = 0
    private var leftOffset = 0
    private var topOffset = 0
    private var rightOffset = 0
    private var bottomOffset = 0
    var effectLeftOffset = 0
    var effectTopOffset = 0
    var effectRightOffset = 0
    var effectBottomOffset = 0
    private val colors = intArrayOf(defaultShadowColor, defaultShadowColor, Color.parseColor("#00000000"), Color.parseColor("#00000000"))
    private var shadowAlpha = defaultShadowStartAlpha
    private var shadowSize = defaultShadowSize
    private var shadowColor = defaultShadowColor
    private var cardBackgroundColor = defaultCardBackgroundColor
    private var cornerRadius = 0
    private var paperCorner = 0
    private var elevationAffectShadowColor = false
    private var elevationAffectShadowSize = false
    private var isFixedContentHeight = false
    private var bindLifeCircle = false
    private var useShadowPool = false
    private var paperSyncCorner = true
    private var isFixedContentWidth = false
    var viewWidth = -3
    var viewHeight = -3
    private val mPath = Path()
    private val highVerPath = Path()
    private val mContentPath = Path()
    private val mShadowPath = Path()
    private val paint = Paint()
    private val bgColorPaint = Paint()
    private val bgPaint = Paint()
    private val pathPaint = Paint()
    private val percent = 0.33f
    private val shadowManager = ShadowManager(colors, percent)
    private var ltCornerRadius = 0
        set(value) {
            field = value
            shadowManager.setCornerRadius(value, IShadow.LEFT_TOP)
        }
    private var rtCornerRadius = 0
        set(value) {
            field = value
            shadowManager.setCornerRadius(value, IShadow.RIGHT_TOP)
        }
    private var rbCornerRadius = 0
        set(value) {
            field = value
            shadowManager.setCornerRadius(value, IShadow.RIGHT_BOTTOM)
        }
    private var lbCornerRadius = 0
        set(value) {
            field = value
            shadowManager.setCornerRadius(value, IShadow.LEFT_BOTTOM)
        }

    private val propertyEffect = Runnable {
        createDrawables()
        postInvalidate()
    }

    private fun realDraw(prompt: Boolean) {
        if (prompt) {
            createDrawables()
            postInvalidate()
        } else {
            removeCallbacks(propertyEffect)
            post(propertyEffect)
        }
    }

    companion object Mode {
        const val ADSORPTION = 0
        const val LINEAR = 1
    }

    init {
        setWillNotDraw(false)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.LCardView, defStyleAttr, 0)
        val indexCount = typedArray.indexCount
        for (i in 0 until indexCount) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.LCardView_shadowSize -> {
                    shadowSize = typedArray.getDimensionPixelSize(index, -1)
                }
                R.styleable.LCardView_shadowColor -> {
                    shadowColor = typedArray.getColor(index, defaultShadowColor)
                }
                R.styleable.LCardView_shadowStartAlpha -> {
                    shadowAlpha = typedArray.getInt(index, defaultShadowStartAlpha)
                }
                R.styleable.LCardView_shadowFluidShape -> {
                    typedArray.getInt(index, ADSORPTION).also { shadowManager.fluidShape = it }
                }
                R.styleable.LCardView_cardBackgroundColor -> {
                    cardBackgroundColor = typedArray.getColor(index, defaultCardBackgroundColor)
                }
                R.styleable.LCardView_curveShadowEffect -> {
                    typedArray.getBoolean(index, false).also { shadowManager.curveShadowEffect = it }
                }
                R.styleable.LCardView_linearBookEffect -> {
                    typedArray.getBoolean(index, false).also { shadowManager.linearBookEffect = it }
                }
                R.styleable.LCardView_cornerRadius -> {
                    cornerRadius = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_paperCorner -> {
                    paperCorner = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_leftTopCornerRadius -> {
                    ltCornerRadius = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_leftBottomCornerRadius -> {
                    lbCornerRadius = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_rightTopCornerRadius -> {
                    rtCornerRadius = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_rightBottomCornerRadius -> {
                    rbCornerRadius = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_elevation -> {
                    cardElevation = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_elevationAffectShadowColor -> {
                    elevationAffectShadowColor = typedArray.getBoolean(index, false)
                }
                R.styleable.LCardView_elevationAffectShadowSize -> {
                    elevationAffectShadowSize = typedArray.getBoolean(index, false)
                }
                R.styleable.LCardView_leftOffset -> {
                    leftOffset = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_rightOffset -> {
                    rightOffset = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_topOffset -> {
                    topOffset = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_bottomOffset -> {
                    bottomOffset = typedArray.getDimensionPixelSize(index, 0)
                }
                R.styleable.LCardView_bookRadius -> {
                    typedArray.getInteger(index, 2).also { shadowManager.setBookRadius(it) }
                }
                R.styleable.LCardView_fixedContentWidth -> {
                    isFixedContentWidth = typedArray.getBoolean(index, false)
                }
                R.styleable.LCardView_fixedContentHeight -> {
                    isFixedContentHeight = typedArray.getBoolean(index, false)
                }
                R.styleable.LCardView_bindLifeCircle -> {
                    bindLifeCircle = typedArray.getBoolean(index, false)
                }
                R.styleable.LCardView_useShadowPool -> {
                    useShadowPool = typedArray.getBoolean(index, false).also { shadowManager.useShadowPool = it }
                }
                R.styleable.LCardView_paperSyncCorner -> {
                    paperSyncCorner = typedArray.getBoolean(index, false)
                }
                R.styleable.LCardView_curvature -> {
                    shadowManager.setCurvature(typedArray.getFloat(index, 4f))
                }
            }
        }
        typedArray.recycle()
        paint.isAntiAlias = true
        paint.isDither = true
        bgPaint.isAntiAlias = true
        bgPaint.isDither = true
        bgColorPaint.isAntiAlias = true
        bgColorPaint.isDither = true
        pathPaint.isDither = true
        pathPaint.isAntiAlias = true
        pathPaint.color = Color.WHITE
        initColors(shadowColor)
        if (elevationAffectShadowSize) {
            shadowSize = cardElevation + 12
        }
        if (cornerRadius != 0) {
            rbCornerRadius = cornerRadius
            rtCornerRadius = rbCornerRadius
            lbCornerRadius = rtCornerRadius
            ltCornerRadius = lbCornerRadius
        }
        initOffset()
        var leftPadding = shadowSize + leftOffset
        leftPadding = max(leftPadding, 0)
        var topPadding = shadowSize + topOffset
        topPadding = max(topPadding, 0)
        var rightPadding = shadowSize + rightOffset
        rightPadding = max(rightPadding, 0)
        var bottomPadding = shadowSize + bottomOffset
        bottomPadding = max(bottomPadding, 0)
        super.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
        if (bindLifeCircle && useShadowPool) {
            bindLifeCircle = shadowManager.bindLifeCircle(context)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!bindLifeCircle && useShadowPool) {
            shadowManager.onAttachedToWindow()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (!bindLifeCircle && useShadowPool) {
            shadowManager.onDetachedFromWindow()
        }
    }

    private fun initOffset() {
        val maxOffset = shadowSize / 2
        leftOffset = min(maxOffset, leftOffset)
        topOffset = min(maxOffset, topOffset)
        rightOffset = min(maxOffset, rightOffset)
        bottomOffset = min(maxOffset, bottomOffset)
        effectLeftOffset = min(leftOffset, 0)
        effectTopOffset = min(topOffset, 0)
        effectRightOffset = min(rightOffset, 0)
        effectBottomOffset = min(bottomOffset, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var wms = widthMeasureSpec
        var hms = heightMeasureSpec
        val widthMode = MeasureSpec.getMode(wms)
        var heightMode: Int
        when (widthMode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                heightMode = ceil(minWidth.toDouble()).toInt()
                wms = MeasureSpec.makeMeasureSpec(max(heightMode, MeasureSpec.getSize(wms)), widthMode)
                heightMode = MeasureSpec.getMode(hms)
                when (heightMode) {
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        val minHeight = ceil(minHeight.toDouble()).toInt()
                        hms = MeasureSpec.makeMeasureSpec(max(minHeight, MeasureSpec.getSize(hms)), heightMode)
                        super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                    }
                    0 -> super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                    else -> super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                }
            }
            MeasureSpec.UNSPECIFIED -> {
                heightMode = MeasureSpec.getMode(hms)
                when (heightMode) {
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        val minHeight = ceil(minHeight.toDouble()).toInt()
                        hms = MeasureSpec.makeMeasureSpec(max(minHeight, MeasureSpec.getSize(hms)), heightMode)
                        super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                    }
                    0 -> super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                    else -> super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                }
            }
            else -> {
                heightMode = MeasureSpec.getMode(hms)
                when (heightMode) {
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        val minHeight = ceil(minHeight.toDouble()).toInt()
                        hms = MeasureSpec.makeMeasureSpec(max(minHeight, MeasureSpec.getSize(hms)), heightMode)
                        super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                    }
                    0 -> super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                    else -> super.onMeasure(if (isFixedContentWidth) 0 else wms, if (isFixedContentHeight) 0 else hms)
                }
            }
        }
        if (viewWidth == -3) {
            viewWidth = measuredWidth
            viewHeight = measuredHeight
            createDrawables()
        }
    }

    private val minHeight: Int
        get() = max(ltCornerRadius, rtCornerRadius) + max(lbCornerRadius, rbCornerRadius)

    private val minWidth: Int
        get() = max(ltCornerRadius, lbCornerRadius) + max(rtCornerRadius, rbCornerRadius)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            viewWidth = w
            viewHeight = h
            createDrawables()
        }
    }

    /**
     * Setting corner radius and shadow size will resize edge of offsets.
     */
    private fun judgeOffset() {
        val minLeftOffset = -(viewWidth / 2 - shadowSize - max(ltCornerRadius, lbCornerRadius))
        if (leftOffset < minLeftOffset) {
            leftOffset = minLeftOffset
        }
        val minTopOffset = -(viewHeight / 2 - shadowSize - max(ltCornerRadius, rtCornerRadius))
        if (topOffset < minTopOffset) {
            topOffset = minTopOffset
        }
        val minRightOffset = -(viewWidth / 2 - shadowSize - max(rtCornerRadius, rbCornerRadius))
        if (rightOffset < minRightOffset) {
            rightOffset = minRightOffset
        }
        val minBottomOffset = -(viewHeight / 2 - shadowSize - max(lbCornerRadius, rbCornerRadius))
        if (bottomOffset < minBottomOffset) {
            bottomOffset = minBottomOffset
        }
    }

    private fun createDrawables() {
        if (viewHeight == -3 || viewWidth == -3) {
            // view is not measure ann not ready to draw
            return
        }
        //4 edges offset effect padding but onSizeChanged doesn't change padding,
        //so I abolish shadowSize change when onSizeChanged is been called. Then
        //the shader will not recreate util you reset any property such as color.
        //If the shadowSize * 4 is larger than view' height or width ,the card's
        //shadow looks not pretty.
//        if (shadowSize > viewHeight / 4) {
//            shadowSize = viewHeight / 4
//        }
        judgeOffset()
        shadowManager.createDrawables(this, shadowSize)
    }

    private fun measureContentPath() {
        shadowManager.measureShadowPath(mShadowPath)
        shadowManager.measureContentPath(this, paperSyncCorner, paperCorner, mContentPath)
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!paperSyncCorner && paperCorner == 0) {
            super.dispatchDraw(canvas)
            return
        }
        canvas.save()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        } else {
            @Suppress("DEPRECATION")
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            pathPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
            super.dispatchDraw(canvas)
            canvas.drawPath(mContentPath, pathPaint)
        } else {
            pathPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            super.dispatchDraw(canvas)
            highVerPath.reset()
            highVerPath.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
            highVerPath.op(mContentPath, Path.Op.DIFFERENCE)
            canvas.drawPath(highVerPath, pathPaint)
        }
        canvas.restore()
        pathPaint.xfermode = null
    }

    override fun onDraw(canvas: Canvas) {
        measureContentPath()
        canvas.save()
        canvas.clipPath(mShadowPath)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(mContentPath)
        } else {
            @Suppress("DEPRECATION")
            canvas.clipPath(mContentPath, Region.Op.DIFFERENCE)
        }
        bgPaint.color = shadowColor
        canvas.drawPath(mShadowPath, bgPaint)
        canvas.restore()
        if (cardBackgroundColor != defaultCardBackgroundColor) {
            bgColorPaint.color = cardBackgroundColor
            canvas.drawPath(mContentPath, bgColorPaint)
        }
        canvas.save()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(mContentPath)
        } else {
            @Suppress("DEPRECATION")
            canvas.clipPath(mContentPath, Region.Op.DIFFERENCE)
        }

        shadowManager.onDraw(canvas, mPath, paint)

        canvas.restore()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        //NO OP
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        //NO OP
    }

    @JvmOverloads
    fun setLeftTopCornerRadius(leftTopCornerRadius: Int, prompt: Boolean = true) {
        var cornerRadius = leftTopCornerRadius
        if (this.ltCornerRadius == cornerRadius) {
            return
        }
        cornerRadius = min(cornerRadius, (viewWidth - paddingLeft - paddingRight) / 2)
        cornerRadius = min(cornerRadius, (viewHeight - paddingTop - paddingBottom) / 2)
        if (this.ltCornerRadius == cornerRadius) {
            return
        }
        this.ltCornerRadius = cornerRadius
        realDraw(prompt)
    }

    @JvmOverloads
    fun setRightTopCornerRadius(rightTopCornerRadius: Int, prompt: Boolean = true) {
        var cornerRadius = rightTopCornerRadius
        if (this.rtCornerRadius == cornerRadius) {
            return
        }
        cornerRadius = min(cornerRadius, (viewWidth - paddingLeft - paddingRight) / 2)
        cornerRadius = min(cornerRadius, (viewHeight - paddingTop - paddingBottom) / 2)
        if (this.rtCornerRadius == cornerRadius) {
            return
        }
        this.rtCornerRadius = cornerRadius
        realDraw(prompt)
    }

    @JvmOverloads
    fun setRightBottomCornerRadius(rightBottomCornerRadius: Int, prompt: Boolean = true) {
        var cornerRadius = rightBottomCornerRadius
        if (this.rbCornerRadius == cornerRadius) {
            return
        }
        cornerRadius = min(cornerRadius, (viewWidth - paddingLeft - paddingRight) / 2)
        cornerRadius = min(cornerRadius, (viewHeight - paddingTop - paddingBottom) / 2)
        if (this.rbCornerRadius == rightBottomCornerRadius) {
            return
        }
        this.rbCornerRadius = cornerRadius
        realDraw(prompt)
    }

    @JvmOverloads
    fun setLeftBottomCornerRadius(leftBottomCornerRadius: Int, prompt: Boolean = true) {
        var cornerRadius = leftBottomCornerRadius
        if (this.lbCornerRadius == cornerRadius) {
            return
        }
        cornerRadius = min(cornerRadius, (viewWidth - paddingLeft - paddingRight) / 2)
        cornerRadius = min(cornerRadius, (viewHeight - paddingTop - paddingBottom) / 2)
        if (this.lbCornerRadius == cornerRadius) {
            return
        }
        this.lbCornerRadius = cornerRadius
        realDraw(prompt)
    }

    @JvmOverloads
    fun setShadowColor(@ColorInt color: Int, prompt: Boolean = true) {
        if (isSameRGB(color)) {
            return
        }
        initColors(color)
        realDraw(prompt)
    }

    private fun isSameRGB(color: Int): Boolean {
        return if (shadowColor == color) {
            true
        } else Color.red(color) == Color.red(shadowColor) && Color.green(color) == Color.green(shadowColor) && Color.blue(color) == Color.blue(shadowColor)
    }

    private fun initColors(@ColorInt color: Int) {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        shadowColor = Color.argb(if (elevationAffectShadowColor) cardElevation + 10 else shadowAlpha, red, green, blue)
        if (shadowManager.fluidShape == ADSORPTION) {
            colors[0] = shadowColor
            colors[1] = Color.argb(Color.alpha(shadowColor) / 4, red, green, blue)
            colors[2] = Color.argb(Color.alpha(shadowColor) / 8, red, green, blue)
            colors[3] = Color.argb(0, red, green, blue)
        } else {
            colors[0] = shadowColor
            colors[1] = Color.argb((Color.alpha(shadowColor) * 0.67).toInt(), red, green, blue)
            colors[2] = Color.argb((Color.alpha(shadowColor) * 0.33).toInt(), red, green, blue)
            colors[3] = Color.argb(0, red, green, blue)
        }
        shadowManager.markColorChange()
    }

    //0~ 255
    @JvmOverloads
    fun setShadowAlpha(alpha: Int, prompt: Boolean = true) {
        if (elevationAffectShadowColor) {
            return
        }
        if (shadowAlpha == alpha) {
            return
        }
        shadowAlpha = alpha
        initColors(shadowColor)
        realDraw(prompt)
    }

    @JvmOverloads
    fun setElevationAffectShadowColor(elevationAffectShadowColor: Boolean, prompt: Boolean = true) {
        if (this.elevationAffectShadowColor != elevationAffectShadowColor) {
            this.elevationAffectShadowColor = elevationAffectShadowColor
            initColors(shadowColor)
            realDraw(prompt)
        }
    }

    @JvmOverloads
    fun setElevationAffectShadowSize(elevationAffectShadowSize: Boolean, prompt: Boolean = true) {
        if (this.elevationAffectShadowSize != elevationAffectShadowSize) {
            this.elevationAffectShadowSize = elevationAffectShadowSize
            if (elevationAffectShadowSize) {
                val shadowSize = cardElevation + 12
                if (this.shadowSize != shadowSize) {
                    this.shadowSize = shadowSize
                    onShadowSizeChange()
                }
            }
            realDraw(prompt)
        }
    }

    @JvmOverloads
    fun setElevation(elevation: Int, prompt: Boolean = true) {
        if (cardElevation == elevation) {
            return
        }
        cardElevation = elevation
        if (elevationAffectShadowColor) {
            initColors(shadowColor)
        }
        if (elevationAffectShadowSize) {
            val shadowSize = elevation + 12
            if (this.shadowSize != shadowSize) {
                this.shadowSize = shadowSize
                onShadowSizeChange()
            }
        }
        realDraw(prompt)
    }

    @JvmOverloads
    fun setCornerRadius(radius: Int, prompt: Boolean = true) {
        if (cornerRadius == radius) {
            return
        }
        cornerRadius = radius
        rbCornerRadius = cornerRadius
        rtCornerRadius = rbCornerRadius
        lbCornerRadius = rtCornerRadius
        ltCornerRadius = lbCornerRadius
        realDraw(prompt)
    }

    @JvmOverloads
    fun setLinearBookEffect(b: Boolean, prompt: Boolean = true) {
        if (shadowManager.linearBookEffect == b) {
            return
        }
        shadowManager.linearBookEffect = b
        realDraw(prompt)
    }

    @JvmOverloads
    fun setCurveShadowEffect(b: Boolean, prompt: Boolean = true) {
        if (shadowManager.curveShadowEffect == b) {
            return
        }
        val shouldReInitColor = shadowManager.fluidShape == ADSORPTION
        shadowManager.curveShadowEffect = b
        if (shouldReInitColor) {
            initColors(shadowColor)
        }
        realDraw(prompt)
    }

    @JvmOverloads
    fun setPaperSyncCorner(b: Boolean, prompt: Boolean = true) {
        if (paperSyncCorner == b) {
            return
        }
        paperSyncCorner = b
        realDraw(prompt)
    }

    @JvmOverloads
    fun setPaperCorner(c: Int, prompt: Boolean = true) {
        if (paperCorner == c) {
            return
        }
        paperCorner = c
        realDraw(prompt)
    }

    @JvmOverloads
    fun setCurvature(curvature: Float, prompt: Boolean = true) {
        shadowManager.setCurvature(curvature)
        realDraw(prompt)
    }

    @JvmOverloads
    fun setBookRadius(r: Int, prompt: Boolean = true) {
        shadowManager.setBookRadius(r)
        realDraw(prompt)
    }

    fun getShadowSize(): Int {
        return shadowSize
    }

    @JvmOverloads
    fun setShadowSize(shadowSize: Int, prompt: Boolean = true) {
        if (elevationAffectShadowSize) {
            //This field make shadow size change with elevation.
            return
        }
        if (this.shadowSize == shadowSize) {
            return
        }
        this.shadowSize = shadowSize
        onShadowSizeChange()
        realDraw(prompt)
    }

    private fun onShadowSizeChange() {
        initOffset()
        var leftPadding = shadowSize + leftOffset
        leftPadding = max(leftPadding, 0)
        var topPadding = shadowSize + topOffset
        topPadding = max(topPadding, 0)
        var rightPadding = shadowSize + rightOffset
        rightPadding = max(rightPadding, 0)
        var bottomPadding = shadowSize + bottomOffset
        bottomPadding = max(bottomPadding, 0)
        val ol = effectLeftOffset
        effectLeftOffset = if (leftPadding == 0) {
            shadowSize + leftOffset
        } else {
            0
        }
        val ot = effectTopOffset
        effectTopOffset = if (topPadding == 0) {
            shadowSize + topOffset
        } else {
            0
        }
        val or = effectRightOffset
        effectRightOffset = if (rightPadding == 0) {
            shadowSize + rightOffset
        } else {
            0
        }
        val ob = effectBottomOffset
        effectBottomOffset = if (bottomPadding == 0) {
            shadowSize + bottomOffset
        } else {
            0
        }

        val needReCreateDrawable = (paddingRight == 0 || rightPadding == 0) || (paddingLeft == 0 || leftPadding == 0)
                || (paddingTop == 0 || topPadding == 0) || (paddingBottom == 0 || bottomPadding == 0)
                || (ol != effectLeftOffset) || (ot != effectTopOffset) || (or != effectRightOffset) || (ob != effectBottomOffset)
        if (needReCreateDrawable) {
            realDraw(false)
        }
        super.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
    }

    fun getShadowColor(): Int {
        return shadowColor
    }

    fun getCardBackgroundColor(): Int {
        return cardBackgroundColor
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun isElevationAffectShadowColor(): Boolean {
        return elevationAffectShadowColor
    }

    fun isElevationAffectShadowSize(): Boolean {
        return elevationAffectShadowSize
    }

    fun getLeftTopCornerRadius(): Int {
        return ltCornerRadius
    }

    fun getRightTopCornerRadius(): Int {
        return rtCornerRadius
    }

    fun getRightBottomCornerRadius(): Int {
        return rbCornerRadius
    }

    fun getLeftBottomCornerRadius(): Int {
        return lbCornerRadius
    }

    fun getShadowAlpha(): Int {
        return shadowAlpha
    }

    fun setCardBackgroundColor(cardBackgroundColor: Int) {
        if (this.cardBackgroundColor == cardBackgroundColor) {
            return
        }
        this.cardBackgroundColor = cardBackgroundColor
        invalidate()
    }

    fun setShadowOffsetCenter(offset: Int) {
        val maxOffset = shadowSize / 2
        val leftOffset = min(maxOffset, offset)
        val rightOffset = min(maxOffset, offset)
        val topOffset = min(maxOffset, offset)
        val bottomOffset = min(maxOffset, offset)
        if (this.leftOffset == leftOffset && this.rightOffset == rightOffset && this.topOffset == topOffset && this.bottomOffset == bottomOffset) {
            return
        }
        this.leftOffset = leftOffset
        this.rightOffset = rightOffset
        this.topOffset = topOffset
        this.bottomOffset = bottomOffset
        onShadowSizeChange()
    }

    fun getLeftOffset(): Int {
        return leftOffset
    }

    fun setLeftOffset(leftOffset: Int) {
        var offset = leftOffset
        val maxOffset = shadowSize / 2
        offset = min(maxOffset, offset)
        if (this.leftOffset == offset) {
            return
        }
        this.leftOffset = offset
        var leftPadding = shadowSize + this.leftOffset
        leftPadding = max(leftPadding, 0)
        val lastEffect = effectLeftOffset
        effectLeftOffset = if (leftPadding == 0) {
            offset + shadowSize
        } else {
            0
        }
        val paddingLeft = paddingLeft
        val needReCreateDrawable : Boolean
        if (paddingLeft != leftPadding) {
            needReCreateDrawable = paddingLeft == 0 || leftPadding == 0
            super.setPadding(leftPadding, paddingTop, paddingRight, paddingBottom)
        } else {
            needReCreateDrawable = effectLeftOffset != lastEffect
        }
        if (needReCreateDrawable) {
            realDraw(false)
        }
    }

    fun getTopOffset(): Int {
        return topOffset
    }

    fun setTopOffset(topOffset: Int) {
        var offset = topOffset
        val maxOffset = shadowSize / 2
        offset = min(maxOffset, offset)
        if (this.topOffset == offset) {
            return
        }
        this.topOffset = offset
        var topPadding = shadowSize + this.topOffset
        topPadding = max(topPadding, 0)
        val lastEffect = effectTopOffset
        effectTopOffset = if (topPadding == 0) {
            this.topOffset + shadowSize
        } else {
            0
        }
        val paddingTop = paddingTop
        val needReCreateDrawable : Boolean
        if (paddingTop != topPadding) {
            needReCreateDrawable = paddingTop == 0 || topPadding == 0
            super.setPadding(paddingLeft, topPadding, paddingRight, paddingBottom)
        } else {
            needReCreateDrawable = effectTopOffset != lastEffect
        }
        if (needReCreateDrawable) {
            realDraw(false)
        }
    }

    fun getRightOffset(): Int {
        return rightOffset
    }

    fun setRightOffset(rightOffset: Int) {
        var offset = rightOffset
        val maxOffset = shadowSize / 2
        offset = min(maxOffset, offset)
        if (this.rightOffset == offset) {
            return
        }
        this.rightOffset = offset
        var rightPadding = shadowSize + this.rightOffset
        rightPadding = max(rightPadding, 0)
        val lastEffect = effectRightOffset
        effectRightOffset = if (rightPadding == 0) {
            this.rightOffset + shadowSize
        } else {
            0
        }
        val needReCreateDrawable : Boolean
        val paddingRight = paddingRight
        if (paddingRight != rightPadding) {
            needReCreateDrawable = paddingRight == 0 || rightPadding == 0
            super.setPadding(paddingLeft, paddingTop, rightPadding, paddingBottom)
        } else {
            needReCreateDrawable = effectRightOffset != lastEffect
        }
        if (needReCreateDrawable) {
            realDraw(false)
        }
    }

    fun getBottomOffset(): Int {
        return bottomOffset
    }

    fun setBottomOffset(bottomOffset: Int) {
        var offset = bottomOffset
        val maxOffset = shadowSize / 2
        offset = min(maxOffset, offset)
        if (this.bottomOffset == offset) {
            return
        }
        this.bottomOffset = offset
        var bottomPadding = shadowSize + this.bottomOffset
        bottomPadding = max(bottomPadding, 0)
        val lastEffect = effectBottomOffset
        effectBottomOffset = if (bottomPadding == 0) {
            this.bottomOffset + shadowSize
        } else {
            0
        }
        val needReCreateDrawable : Boolean
        val paddingBottom = paddingBottom
        if (paddingBottom != bottomPadding) {
            needReCreateDrawable = paddingBottom == 0 || bottomPadding == 0
            super.setPadding(paddingLeft, paddingTop, paddingRight, bottomPadding)
        } else {
            needReCreateDrawable = effectBottomOffset != lastEffect
        }
        if (needReCreateDrawable) {
            realDraw(false)
        }
    }

    @JvmOverloads
    fun setShadowFluidShape(shape: Int, prompt: Boolean = true) {
        if (shadowManager.onShapeModeChange(shape)) {
            initColors(shadowColor)
            realDraw(prompt)
        }
    }

    fun properties(): Property {
        return Property()
    }

    /**
     * Shadow should be recreate only once after multi property changed.
     */
    inner class Property {

        fun leftTopCornerRadius(r: Int): Property {
            setLeftTopCornerRadius(r, false)
            return this
        }

        fun rightTopCornerRadius(r: Int): Property {
            setRightTopCornerRadius(r, false)
            return this
        }

        fun rightBottomCornerRadius(r: Int): Property {
            setRightBottomCornerRadius(r, false)
            return this
        }

        fun leftBottomCornerRadius(r: Int): Property {
            setLeftBottomCornerRadius(r, false)
            return this
        }

        fun shadowColor(@ColorInt c: Int): Property {
            setShadowColor(c, false)
            return this
        }

        fun shadowAlpha(alpha: Int): Property {
            setShadowAlpha(alpha, false)
            return this
        }

        fun shadowSize(s: Int): Property {
            setShadowSize(s, false)
            return this
        }

        fun elevation(e: Int): Property {
            setElevation(e, false)
            return this
        }

        fun cornerRadius(cr: Int): Property {
            setCornerRadius(cr, false)
            return this
        }

        fun shadowFluidShape(m: Int): Property {
            setShadowFluidShape(m, false)
            return this
        }

        fun elevationAffectShadowColor(b: Boolean): Property {
            setElevationAffectShadowColor(b, false)
            return this
        }

        fun elevationAffectShadowSize(b: Boolean): Property {
            setElevationAffectShadowSize(b, false)
            return this
        }

        fun linearBookEffect(b: Boolean): Property {
            setLinearBookEffect(b, false)
            return this
        }

        fun curveShadowEffect(b: Boolean): Property {
            setCurveShadowEffect(b, false)
            return this
        }

        fun paperSyncCorner(b: Boolean): Property {
            setPaperSyncCorner(b, false)
            return this
        }

        fun bookRadius(r: Int): Property {
            setBookRadius(r, false)
            return this
        }

        fun paperCorner(r: Int): Property {
            setPaperCorner(r, false)
            return this
        }

        fun curvature(curvature: Float): Property {
            setCurvature(curvature, false)
            return this
        }
    }
}