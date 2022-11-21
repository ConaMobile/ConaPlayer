package com.conamobile.conaplayer.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.conamobile.appthemehelper.util.ATHUtil
import com.conamobile.conaplayer.extensions.accentColor
import com.conamobile.conaplayer.extensions.addAlpha
import com.conamobile.conaplayer.extensions.setItemColors
import com.conamobile.conaplayer.util.PreferenceUtil
import com.google.android.material.navigationrail.NavigationRailView

class TintedNavigationRailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : NavigationRailView(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode) {
            labelVisibilityMode = PreferenceUtil.tabTitleMode

            if (!PreferenceUtil.materialYou) {
                val iconColor = ATHUtil.resolveColor(context, android.R.attr.colorControlNormal)
                val accentColor = context.accentColor()
                setItemColors(iconColor, accentColor)
                itemRippleColor = ColorStateList.valueOf(accentColor.addAlpha(0.08F))
                itemActiveIndicatorColor = ColorStateList.valueOf(accentColor.addAlpha(0.12F))
            }
        }
    }
}