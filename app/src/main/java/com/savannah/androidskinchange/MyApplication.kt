package com.savannah.androidskinchange

import android.app.Application
import com.savannah.skinlibrary.SkinManager

/**
 * Author:Savannah
 * Description:
 * AndroidSkinChange 10/15/20
 */
class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.instance.init(this)
    }

}