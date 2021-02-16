package com.mage.uiviewdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mage.uiviewdemo.R

/**
 * 实现Nescrolling机制的父控件，子控件是NestScrollingView
 */
class NestScrollingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_scrolling)
    }
}