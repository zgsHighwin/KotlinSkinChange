package com.savannah.skinlibrary.util

import android.R
import android.app.Activity
import android.os.Build

object StatusBarUtils {
    fun forStatusBar(activity: Activity) {
        val a = activity.theme.obtainStyledAttributes(
            0, intArrayOf(
                R.attr.statusBarColor
            )
        )
        val color = a.getColor(0, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
        a.recycle()
    }

    fun forStatusBar(activity: Activity, skinColor: Int) {
        activity.window.statusBarColor = skinColor
    }
}