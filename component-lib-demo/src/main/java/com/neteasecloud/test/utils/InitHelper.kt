package com.neteasecloud.test.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object InitHelper {

    fun <VM> getViewModelClz(genericSuperclass: Type?): Class<VM> {
        return if (genericSuperclass is ParameterizedType) {
            //参数化类型
            //返回表示此类型实际类型参数的 Type 对象的数组
            val actualTypeArguments = genericSuperclass.actualTypeArguments
            actualTypeArguments[1] as Class<VM>
        } else {
            genericSuperclass as Class<VM>
        }
    }
}