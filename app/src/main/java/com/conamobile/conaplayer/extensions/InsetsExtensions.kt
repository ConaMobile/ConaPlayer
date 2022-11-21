package com.conamobile.conaplayer.extensions

import androidx.core.view.WindowInsetsCompat
import com.conamobile.conaplayer.util.PreferenceUtil
import com.conamobile.conaplayer.util.RetroUtil

fun WindowInsetsCompat?.getBottomInsets(): Int {
    return if (PreferenceUtil.isFullScreenMode) {
        return 0
    } else {
        this?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom
            ?: RetroUtil.navigationBarHeight
    }
}
