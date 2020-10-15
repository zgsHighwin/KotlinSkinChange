package com.savannah.skinlibrary.core

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatViewInflater
import com.savannah.skinlibrary.view.SkinnableButton
import com.savannah.skinlibrary.view.SkinnableTextView

/**
 * Author:Savannah
 * Description:
 * AndroidSkinChange 10/14/20
 */
class SkinAppCompatViewInflater(
    private val context: Context
) :
    AppCompatViewInflater() {
    private lateinit var name /* 控件名*/: String
    private lateinit var attrs /* 某控件对应所有属性*/: AttributeSet

    fun setName(name: String) {
        this.name = name
    }

    fun setAttrs(attrs: AttributeSet) {
        this.attrs = attrs
    }

    fun skinView(): View? {
        val view: View? = null
        Log.i(TAG, "skinView: $name")
        when (name) {
//            "Button" -> return SkinnableButton(context, attrs)
            "TextView" -> return SkinnableTextView(context, attrs)
        }

        return view
    }

    companion object {
        private const val TAG = "SkinApp"
    }
}
