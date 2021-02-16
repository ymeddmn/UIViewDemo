package com.mage.uiviewdemo.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginTop

/**
 * author  :mayong
 * function:
 * date    :2021/2/10
 **/
class CustomScrollView : LinearLayout {
    private var maxScrollY: Int = 0
    private var childHeight: Int = 0
    private var obtain: VelocityTracker? = null
    private var mMinimumVelocity: Int//最小滑动距离
    private var lasty: Float = 0f
    val scroller: Scroller

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        scroller = Scroller(context,AccelerateInterpolator())
//        scroller = Scroller(context,AccelerateDecelerateInterpolator())
//        scroller.extendDuration(10)
        val configuration: ViewConfiguration = ViewConfiguration
            .get(getContext())
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        children.forEachIndexed { index, view ->
            view as TextView
            view.text = index.toString()
        }
        log("子view数量： $childCount")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        childHeight = 0
        children.forEachIndexed { index, view ->
            childHeight += view.measuredHeight + view.marginTop + view.marginBottom
        }
        maxScrollY = childHeight - measuredHeight//内部子控件的最大可滑动高度==（所有子控件的高度-父控件高度）
        log("控件高度：${MeasureSpec.getSize(heightMeasureSpec)}  子控件高度：$childHeight")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        fliping(event)
        return true
    }

    private fun fliping(event: MotionEvent?) {
        if (obtain == null) {
            obtain = VelocityTracker.obtain()
        }
        obtain?.addMovement(event)
        obtain?.computeCurrentVelocity(1000)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lasty = event.y
                if (!scroller.isFinished) {//如果正在滑动，则结束滑动
                    scroller.forceFinished(true)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = (event.y - lasty)
                if (scrollY in 0..maxScrollY) {//只有在可滑动范围内才能滑动
                    if (dy < 0) {//向上滑动
                        if (scrollY - dy > maxScrollY) {//滑到底了，再滑最多只能滑到maxScrollY的位置
                            scrollTo(0, maxScrollY)
                            log("滑到底了 ${scrollY - dy}")
                        } else {
                            scrollBy(0, -dy.toInt())
                            log("向下滑动中")
                        }
                    } else if (dy == 0f) {
                        if (dy > 0) {//当scrolly==0的时候，只有向下滑动才能滑动
                            scrollBy(0, -dy.toInt())
                        }
                    } else {//向下滑动
                        if (scrollY - dy < 0 && dy > 0) {//如果scrolly小于dy则默认滑动的y方向0的位置
                            scrollTo(0, 0)
                        } else {
                            scrollBy(0, -dy.toInt())
                        }
                    }
                }
                lasty = event.y
            }
            MotionEvent.ACTION_UP -> {
                obtain?.run {
                    /**
                     * scrollY：滑动的起始位置，
                     * yVelocity：y方向的速度，
                     * maxScrollY：滑动的最大距离
                     */
                    scroller.fling(scrollX, scrollY, 0, -yVelocity.toInt(), 0, 0, 0, maxScrollY)
                }
                postInvalidateOnAnimation()
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.currY)
            postInvalidate()
        }
    }

    fun log(msg: String) {
        Log.e("日志：", msg)
    }
}