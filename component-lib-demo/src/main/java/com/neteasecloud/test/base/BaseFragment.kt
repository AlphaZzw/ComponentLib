package com.neteasecloud.test.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neteasecloud.test.BR
import com.neteasecloud.test.utils.InitHelper

abstract class BaseFragment<T : ViewDataBinding, VM : ViewModel> : Fragment() {
    private val TAG = javaClass.simpleName
    protected var binding: T? = null
    protected var vm: VM? = null


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "onHiddenChanged isVisible = ${!hidden}")
    }

    @LayoutRes
    protected abstract fun getLayout(): Int
    protected abstract fun initView()
    open fun handleParameters(bundle: Bundle?) {
        (vm as? BaseViewModel)?.apply {
            activity?.apply {
                initData(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "onCreateView $savedInstanceState")
        if (binding?.root != null) {
            return binding?.root
        }
        val layoutId = getLayout()
        val rootView = inflater.inflate(layoutId, container, false)
        binding = DataBindingUtil.bind(rootView)
        binding?.apply {
            lifecycleOwner = this@BaseFragment
            setVariable(BR.vm, vm)
        }
        return binding?.root
    }

    open fun onInitViewModel() {
        try {
            val vmClazz = InitHelper.getViewModelClz<VM>(javaClass.genericSuperclass)
            vm = ViewModelProvider(this)[vmClazz]
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated $savedInstanceState")
        initView()
        handleParameters(arguments)
    }
}