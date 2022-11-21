package com.conamobile.conaplayer.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.transition.Transition
import com.conamobile.appthemehelper.util.ATHUtil
import com.conamobile.conaplayer.glide.palette.BitmapPaletteTarget
import com.conamobile.conaplayer.glide.palette.BitmapPaletteWrapper
import com.conamobile.conaplayer.util.ColorUtil

abstract class SingleColorTarget(view: ImageView) : BitmapPaletteTarget(view) {

    private val defaultFooterColor: Int
        get() = ATHUtil.resolveColor(view.context, androidx.appcompat.R.attr.colorControlNormal)

    abstract fun onColorReady(color: Int)

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        onColorReady(defaultFooterColor)
    }

    override fun onResourceReady(
        resource: BitmapPaletteWrapper,
        transition: Transition<in BitmapPaletteWrapper>?,
    ) {
        super.onResourceReady(resource, transition)
        onColorReady(
            ColorUtil.getColor(
                resource.palette,
                ATHUtil.resolveColor(view.context, androidx.appcompat.R.attr.colorPrimary)
            )
        )
    }
}
