package com.conamobile.appthemehelper.util

import android.R
import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.core.content.res.use
import java.lang.Exception

object ATHUtil {

    fun isWindowBackgroundDark(context: Context): Boolean {
        return !ColorUtil.isColorLight(resolveColor(context, R.attr.windowBackground))
    }

    @JvmOverloads
    fun resolveColor(context: Context, @AttrRes attr: Int, fallback: Int = 0): Int {
        context.theme.obtainStyledAttributes(intArrayOf(attr)).use {
            return try {
                it.getColor(0, fallback);
            } catch (e: Exception) {
                Color.BLACK
            }
        }
    }
}