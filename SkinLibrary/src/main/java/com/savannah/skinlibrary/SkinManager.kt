package com.savannah.skinlibrary

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.core.content.res.ResourcesCompat
import com.savannah.skinlibrary.model.SkinCache
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

/**
 * Author:Savannah
 * Description:
 * AndroidSkinChange 10/15/20
 */


class SkinManager {

    //    companion object {
//        val instance: SkinManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SkinManager() }
//    }
    private lateinit var context: Context
    private lateinit var appResources: Resources
    private lateinit var skinResources: Resources
    private lateinit var skinPackageName: String
    private lateinit var cacheSkin: MutableMap<String, SkinCache>
    private var isDefaultSkin: Boolean = true


    companion object {
        private const val ADD_ASSET_PATH = "addAssetPath";
        val instance = Singleton.holder
    }

    fun init(application: Application) {
        this.context = application;
        this.appResources = application.resources
        cacheSkin = HashMap()
    }

    private object Singleton {
        val holder = SkinManager()
    }

    /**
     * 加载皮肤包资源
     *
     * @param skinPath 皮肤包路径，为空则加载app内置资源
     */
    fun loaderSkinResources(skinPath: String?) {
        if (TextUtils.isEmpty(skinPath)) {
            isDefaultSkin = true
            return
        }
        if (cacheSkin.containsKey(skinPath)) {
            isDefaultSkin = false
            val skinCache = cacheSkin[skinPath]
            skinResources = skinCache!!.skinResources
            skinPackageName = skinCache.skinPackageName
            return
        }

        try {
            // 创建资源管理器（此处不能用：application.getAssets()）
            val assetManager = AssetManager::class.java.newInstance()
            // 由于AssetManager中的addAssetPath和setApkAssets方法都被@hide，目前只能通过反射去执行方法
            val addAssetPathInternal = assetManager.javaClass.getDeclaredMethod(
                ADD_ASSET_PATH,
                String::class.java
            )
            addAssetPathInternal.isAccessible = true
            addAssetPathInternal.invoke(assetManager, skinPath)
            // 创建加载外部的皮肤包()文件Resources（注：依然是本应用加载）
            skinResources =
                Resources(assetManager, appResources.displayMetrics, appResources.configuration)
            skinPackageName = context.packageManager.getPackageArchiveInfo(
                skinPath!!,
                PackageManager.GET_ACTIVITIES
            )!!.packageName
            // 无法获取皮肤包应用的包名，则加载app内置资源
            isDefaultSkin = TextUtils.isEmpty(skinPackageName)
            if (!isDefaultSkin) {
                cacheSkin[skinPath] = SkinCache(skinResources, skinPackageName)
            }
        } catch (e: Exception) {
            // 发生异常，预判：通过skinPath获取skinPacakageName失败！
            isDefaultSkin = true
        }
    }


    /**
     * 参考：resources.arsc资源映射表
     * 通过ID值获取资源 Name 和 Type
     *
     * @param resourceId 资源ID值
     * @return 如果没有皮肤包则加载app内置资源ID，反之加载皮肤包指定资源ID
     */
    private fun getSkinResourceIds(resourceId: Int): Int {
        // 优化：如果没有皮肤包或者没做换肤动作，直接返回app内置资源！
        if (isDefaultSkin) return resourceId
        // 使用app内置资源加载，是因为内置资源与皮肤包资源一一对应（“netease_bg”, “drawable”）
        val resourceName = appResources.getResourceEntryName(resourceId)
        val resourceType = appResources.getResourceTypeName(resourceId)

        // 动态获取皮肤包内的指定资源ID
        // getResources().getIdentifier(“netease_bg”, “drawable”, “com.netease.skin.packages”);
        val skinResourceId =
            skinResources.getIdentifier(resourceName, resourceType, skinPackageName)

        isDefaultSkin = skinResourceId == 0
        return if (skinResourceId == 0) resourceId else skinResourceId
    }

    /**
     * 是否是默认的皮肤
     */
    fun isDefaultSkin(): Boolean {
        return isDefaultSkin
    }

    fun getColor(resourceId: Int): Int {
        val ids = getSkinResourceIds(resourceId)
        return if (isDefaultSkin) appResources.getColor(ids) else skinResources.getColor(ids)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun getColorStateList(resourceId: Int): ColorStateList {
        val ids = getSkinResourceIds(resourceId)
        return if (isDefaultSkin) appResources.getColorStateList(ids) else skinResources.getColorStateList(
            ids
        )
    }

    fun getDrawableOrMipMap(resourceId: Int): Drawable {
        val ids = getSkinResourceIds(resourceId)
        return if (isDefaultSkin) appResources.getDrawable(ids) else skinResources.getDrawable(ids)
    }

    fun getString(resourceId: Int): String {
        val ids = getSkinResourceIds(resourceId)
        return if (isDefaultSkin) appResources.getString(ids) else skinResources.getString(ids)
    }

    // 返回值特殊情况：可能是color / drawable / mipmap
    fun getBackgroundOrSrc(resourceId: Int): Any? {
        // 需要获取当前属性的类型名Resources.getResourceTypeName(resourceId)再判断
        val resourceTypeName = appResources.getResourceTypeName(resourceId)
        when (resourceTypeName) {
            "color" -> return getColor(resourceId)
            "mipmap", "drawable" -> return getDrawableOrMipMap(resourceId)
        }
        return null
    }

    // 获得字体
    fun getTypeface(resourceId: Int): Typeface {
        // 通过资源ID获取资源path，参考：resources.arsc资源映射表
        val skinTypefacePath = getString(resourceId)
        //路径为空，使用系统默认字体
        if (TextUtils.isEmpty(skinTypefacePath)) return Typeface.DEFAULT
        return if (isDefaultSkin) Typeface.createFromAsset(
            appResources.assets,
            skinTypefacePath
        ) else Typeface.createFromAsset(skinResources.assets, skinTypefacePath)
    }

}

fun main(args: Array<String>) {
}
