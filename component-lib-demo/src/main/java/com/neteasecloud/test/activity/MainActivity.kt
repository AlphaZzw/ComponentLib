package com.neteasecloud.test.activity

import android.os.Bundle
import com.neteasecloud.test.R
import com.neteasecloud.test.base.BaseActivity
import com.neteasecloud.test.databinding.ActivityMainBinding
import com.neteasecloud.test.model.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
    }
}