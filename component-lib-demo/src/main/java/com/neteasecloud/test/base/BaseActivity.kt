package com.neteasecloud.test.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.neteasecloud.test.BR
import com.neteasecloud.test.ComponentApplication
import com.neteasecloud.test.utils.Dog
import com.neteasecloud.test.utils.InitHelper

abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    protected var binding: T? = null
    protected var vm: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ComponentApplication.instance().isRD()) {
            val configuration = resources.configuration
            Log.d(TAG, "configuration = " + configuration + "; mnc " + configuration.mnc)
            configuration.mnc = 200
            resources.updateConfiguration(configuration, resources.displayMetrics)
            Log.d(TAG, "configuration = " + configuration + "; mnc " + configuration.mnc)
        }
        super.onCreate(savedInstanceState)
        val rootView = layoutInflater.inflate(getLayoutId(), null)
        binding = DataBindingUtil.bind(rootView)
        try {
            val vmClazz = InitHelper.getViewModelClz<VM>(javaClass.genericSuperclass)
            vm = ViewModelProvider(this)[vmClazz]
            vm?.initData(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        vm?.apply {
            binding?.lifecycleOwner = this@BaseActivity
            binding?.setVariable(BR.vm, this)
        }
        setContentView(rootView)
        initView()
        Dog.d(TAG,"onCreate")
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    open fun setStatusBar() {
        val window = window
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // 判断api版本为4.3(18) 则设置上下bar透明属性，需要rom配合修改
            val attributes = window.attributes
            val flagTranslucentNavigation =
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS //0x80000000;
            val flagTranslucentStatus =
                WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE //0x10000000;
            attributes.flags =
                attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
            window.attributes = attributes
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
        }
    }
}