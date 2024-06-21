package com.neteasecloud.test.activity

import android.app.Activity
import android.os.Bundle
import com.neteasecloud.test.R

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}