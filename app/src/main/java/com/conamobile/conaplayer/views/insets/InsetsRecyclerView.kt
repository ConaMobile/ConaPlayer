package com.conamobile.conaplayer.views.insets

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.conamobile.conaplayer.util.PreferenceUtil
import dev.chrisbanes.insetter.applyInsetter

class InsetsRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr) {
    init {
        if (!isInEditMode && !PreferenceUtil.isFullScreenMode)
            applyInsetter {
                type(navigationBars = true) {
                    padding(vertical = true)
                }
            }
    }
}