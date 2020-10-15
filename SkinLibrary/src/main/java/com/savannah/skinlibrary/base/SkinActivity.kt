package com.savannah.skinlibrary.base

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.savannah.skinlibrary.SkinManager
import com.savannah.skinlibrary.core.SkinAppCompatViewInflater
import com.savannah.skinlibrary.core.SkinView
import com.savannah.skinlibrary.util.ActionBarUtils
import com.savannah.skinlibrary.util.NavigationUtils
import com.savannah.skinlibrary.util.StatusBarUtils

/**
 * Author:Savannah
 * Description:
 * AndroidSkinChange 10/14/20
 */
open class SkinActivity : AppCompatActivity() {
    private var skinAppCompatViewInflater: SkinAppCompatViewInflater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(this)
        LayoutInflaterCompat.setFactory2(layoutInflater, this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        Log.i(Companion.TAG, "onCreateView: $name")
        if (openChangeSkin()) {
            if (skinAppCompatViewInflater == null) {
                skinAppCompatViewInflater = SkinAppCompatViewInflater(context)
            }
            skinAppCompatViewInflater!!.setAttrs(attrs)
            skinAppCompatViewInflater!!.setName(name)
            return skinAppCompatViewInflater!!.skinView()
        }
        return super.onCreateView(parent, name, context, attrs)
    }


    /**
     * 动态换肤（api限制：5.0版本）
     */
    protected open fun skinDynamic(skinPath: String?, themeColorId: Int) {
        SkinManager.instance.loaderSkinResources(skinPath)
        if (themeColorId != 0) {
            val themeColor: Int = SkinManager.instance.getColor(themeColorId)
            StatusBarUtils.forStatusBar(this, themeColor)
            NavigationUtils.forNavigation(this, themeColor)
            ActionBarUtils.forActionBar(this, themeColor)
        }
        applyViews(window.decorView)
    }

    /**
     * 控件回调监听，匹配上则给控件执行换肤方法
     */
    protected open fun applyViews(view: View) {
        if (view is SkinView) {
            val viewsMatch: SkinView = view as SkinView
            viewsMatch.skinView()
        }
        if (view is ViewGroup) {
            val parent = view
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                applyViews(parent.getChildAt(i))
            }
        }
    }

    protected open fun defaultSkin(themeColorId: Int) {
        skinDynamic(null, themeColorId)
    }

    /**
     * 是否是可以修改皮肤的
     * @return true表示可以修改皮肤，false表示不可以修改皮肤
     */
    protected open fun openChangeSkin(): Boolean {
        return false
    }

    companion object {
        private const val TAG = "SkinActivity"
    }
}