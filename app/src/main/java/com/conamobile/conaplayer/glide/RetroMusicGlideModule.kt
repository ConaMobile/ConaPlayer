package com.conamobile.conaplayer.glide

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.conamobile.conaplayer.glide.artistimage.ArtistImage
import com.conamobile.conaplayer.glide.artistimage.Factory
import com.conamobile.conaplayer.glide.audiocover.AudioFileCover
import com.conamobile.conaplayer.glide.audiocover.AudioFileCoverLoader
import com.conamobile.conaplayer.glide.palette.BitmapPaletteTranscoder
import com.conamobile.conaplayer.glide.palette.BitmapPaletteWrapper
import com.conamobile.conaplayer.glide.playlistPreview.PlaylistPreview
import com.conamobile.conaplayer.glide.playlistPreview.PlaylistPreviewLoader
import java.io.InputStream

@GlideModule
class RetroMusicGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(PlaylistPreview::class.java,
            Bitmap::class.java,
            PlaylistPreviewLoader.Factory(context))
        registry.prepend(
            AudioFileCover::class.java,
            InputStream::class.java,
            AudioFileCoverLoader.Factory()
        )
        registry.prepend(ArtistImage::class.java, InputStream::class.java, Factory(context))
        registry.register(
            Bitmap::class.java,
            BitmapPaletteWrapper::class.java, BitmapPaletteTranscoder()
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}