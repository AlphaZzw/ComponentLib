package com.neteasecloud.test

import android.app.Application
import com.neteasecloud.componentlib.utils.SystemPropertiesProxy
import com.neteasecloud.test.utils.Dog

class ComponentApplication : Application() {

    private var mRD = false

    override fun onCreate() {
        super.onCreate()
        Dog.initLog(this)
        Dog.d(TAG, "onCreate")
        instance = this
        val isRD: Int =
            SystemPropertiesProxy.getInt("persist.sys.rudder", 0)
        mRD = isRD == 2
    }

    fun isRD(): Boolean {
        return mRD
    }

    companion object {
        private const val TAG = "ComponentApplication"

        private lateinit var instance: ComponentApplication
        fun instance() = instance
    }
}