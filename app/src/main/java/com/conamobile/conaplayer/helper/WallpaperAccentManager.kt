package com.conamobile.conaplayer.helper

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.conamobile.appthemehelper.ThemeStore
import com.conamobile.appthemehelper.util.VersionUtils
import com.conamobile.conaplayer.util.PreferenceUtil

class WallpaperAccentManager(val context: Context) {

    private val onColorsChangedListener by lazy {
        WallpaperManager.OnColorsChangedListener { _, _ ->
            updateColors()
        }
    }

    fun init() {
        if (VersionUtils.hasOreoMR1()) {
            with(WallpaperManager.getInstance(context)) {
                updateColors()
                if (PreferenceUtil.wallpaperAccent) {
                    addOnColorsChangedListener(
                        onColorsChangedListener,
                        Handler(Looper.getMainLooper())
                    )
                }
            }
        }
    }

    fun release() {
        if (VersionUtils.hasOreoMR1()) {
            WallpaperManager.getInstance(context)
                .removeOnColorsChangedListener(onColorsChangedListener)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateColors() {
        if (VersionUtils.hasOreoMR1()) {
            val colors = WallpaperManager.getInstance(context)
                .getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
            if (colors != null) {
                val primaryColor = colors.primaryColor.toArgb()
                ThemeStore.editTheme(context).wallpaperColor(context, primaryColor).commit()
            }
        }
    }
}