package com.savannah.androidskinchange

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.savannah.skinlibrary.base.SkinActivity
import java.io.File

class MainActivity : SkinActivity() {
    private val TAG = "MainActivity"
    private lateinit var sdkPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sdkPath =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "SkinTest/net163.skin"
        Log.i(TAG, "onCreate: $sdkPath");
    }

    fun skin(view: View) {
        skinDynamic(sdkPath, R.color.skin_item_color)
    }

    fun defaultSkinClick(view: View) {
        defaultSkin(R.color.colorPrimary)
    }

    override fun openChangeSkin(): Boolean {
        return true
    }
}

