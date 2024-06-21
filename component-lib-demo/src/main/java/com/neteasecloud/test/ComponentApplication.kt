package com.neteasecloud.test

import android.app.Application
import com.neteasecloud.test.utils.Dog

class ComponentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Dog.initLog(this)
        Dog.d(TAG, "onCreate")
    }

    companion object {
        private const val TAG = "ComponentApplication"
    }
}