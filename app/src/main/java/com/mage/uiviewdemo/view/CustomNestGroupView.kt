package com.mage.uiviewdemo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.*
import androidx.core.widget.NestedScrollView
import com.mage.uiviewdemo.R

/**
 * author  :mayong
 * function:
 * date    :2021/2/16
 **/
class CustomNestGroupView : RelativeLayout, NestedScrollingParent3 {
    private var titleMinY: Float = 0f//title的最小transY坐标
    private var titleMaxY: Float = 0f//title的最大transY坐标
    private var contentView: View? = null
    private var titleHeight: Int = 0
    private var titleView: View? = null
    val helper: NestedScrollingParentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        titleView = getChildAt(0)
        contentView = getChildAt(1)
        titleHeight = titleView?.measuredHeight ?: 0
        titleMinY = -titleHeight.toFloat()
        titleMaxY = 0f
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<LinearLayout>(R.id.container).let {
            for (i in 0 until it.childCount - 1) {
                var view = it.getChildAt(i) as TextView
                view.text = i.toString()
            }
        }
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        helper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return child is NestedScrollView
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        var titleTransY: Float = titleView?.translationY ?: 0f
        var contentTransY: Float = contentView?.translationY ?: 0f
        if (dy > 0) {//向上滑动
            if (titleTransY in titleMinY..titleMaxY) {//title在可滑动范围内
                if (titleTransY - dy <= titleMinY) {//如果预期tranxY值小于最小值，那么直接设置为最小值
                    titleView?.translationY = -titleHeight.toFloat()
                    contentView?.translationY = 0f
                    consumed[1]= (titleTransY-titleMinY).toInt()
                } else {//设置成预期值
                    titleView?.translationY = titleTransY - dy
                    contentView?.translationY = contentTransY - dy
                    consumed[1] =
                        dy//这里消费了dy的距离，所以这样通知子控件后，子控件NestScrollView将不会滑动，如果这里不设置消费距离，则子控件也会跟着滑动
                }
            }
        } else {//x向下滑动
            if (titleTransY >= -titleHeight && titleTransY < 0) {
                if (titleTransY - dy > 0) {
                    titleView?.translationY = 0f
                    contentView?.translationY = titleHeight.toFloat()
                } else {
                    titleView?.translationY = titleTransY - dy
                    contentView?.translationY = contentTransY - dy
                    consumed[1] =
                        dy//这里消费了dy的距离，所以这样通知子控件后，子控件NestScrollView将不会滑动，如果这里不设置消费距离，则子控件也会跟着滑动
                }
            }

        }
    }

    private fun log(msg: String) {
        Log.e("日志", msg)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {

    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        helper.onStopNestedScroll(target, type)
    }


}