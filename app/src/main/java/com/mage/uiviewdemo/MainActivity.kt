package com.mage.uiviewdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mage.uiviewdemo.activity.CustomScrollViewActivity

/**
 * 自定义的一个ScrollView
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_scroll).setOnClickListener {
            startActivity(Intent(this, CustomScrollViewActivity::class.java))
        }
    }
}