package com.conamobile.conaplayer.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.transition.Transition
import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.extensions.colorControlNormal
import com.conamobile.conaplayer.glide.palette.BitmapPaletteTarget
import com.conamobile.conaplayer.glide.palette.BitmapPaletteWrapper
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor

abstract class RetroMusicColoredTarget(view: ImageView) : BitmapPaletteTarget(view) {

    protected val defaultFooterColor: Int
        get() = getView().context.colorControlNormal()

    abstract fun onColorReady(colors: MediaNotificationProcessor)

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        onColorReady(MediaNotificationProcessor.errorColor(App.getContext()))
    }

    override fun onResourceReady(
        resource: BitmapPaletteWrapper,
        transition: Transition<in BitmapPaletteWrapper>?,
    ) {
        super.onResourceReady(resource, transition)
        MediaNotificationProcessor(App.getContext()).getPaletteAsync({
            onColorReady(it)
        }, resource.bitmap)
    }
}
