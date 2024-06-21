package com.neteasecloud.test.base

import android.content.Context
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract fun initData(context: Context)
}