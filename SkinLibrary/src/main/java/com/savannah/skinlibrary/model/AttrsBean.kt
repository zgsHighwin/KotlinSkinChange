package com.savannah.skinlibrary.model

import android.content.res.TypedArray
import android.util.Log
import com.savannah.skinlibrary.R

/**
 * Author:Savannah
 * Description:
 * AndroidSkinChange 10/15/20
 */

class AttrsBean {

    private var resourcesMap: MutableMap<Int, Int> = HashMap()

    var isSkinSupport: Boolean = true


    companion object {
        private const val DEFAULT_VALUE = -1
        private const val TAG = "AttrsBean";
    }

    /**
     * 储控件的key、value
     *
     * @param typedArray 控件属性的类型集合，如：background / textColor
     * @param styleable  自定义属性，参考value/attrs.xml
     */
    fun saveViewResource(typedArray: TypedArray, styleable: IntArray) {
        isSkinSupport = typedArray.getBoolean(R.styleable.SkinnableTextView_skinSupport, true)

        styleable.forEachIndexed { index, i ->
            val resourceId = typedArray.getResourceId(index, DEFAULT_VALUE)
            resourcesMap[i] = resourceId
        }
    }

    /**
     * 获取控件某属性的resourceId
     *
     * @param styleable 自定义属性，参考value/attrs.xml
     * @return 某控件某属性的resourceId
     */
    fun getViewResource(styleable: Int): Int {
        return resourcesMap[styleable]!!
    }

    fun saveResourceId(resourceId: Int) {
        resourcesMap[resourceId] = resourceId
    }

}