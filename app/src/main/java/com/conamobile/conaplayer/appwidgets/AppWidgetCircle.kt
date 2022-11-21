package com.conamobile.conaplayer.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.conamobile.appthemehelper.util.MaterialValueHelper
import com.conamobile.appthemehelper.util.VersionUtils
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.activities.MainActivity
import com.conamobile.conaplayer.appwidgets.base.BaseAppWidget
import com.conamobile.conaplayer.extensions.getTintedDrawable
import com.conamobile.conaplayer.glide.GlideApp
import com.conamobile.conaplayer.glide.RetroGlideExtension
import com.conamobile.conaplayer.glide.palette.BitmapPaletteWrapper
import com.conamobile.conaplayer.service.MusicService
import com.conamobile.conaplayer.service.MusicService.Companion.ACTION_TOGGLE_PAUSE
import com.conamobile.conaplayer.service.MusicService.Companion.TOGGLE_FAVORITE
import com.conamobile.conaplayer.util.MusicUtil
import com.conamobile.conaplayer.util.PreferenceUtil
import com.conamobile.conaplayer.util.RetroUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AppWidgetCircle : BaseAppWidget() {
    private var target: Target<BitmapPaletteWrapper>? = null // for cancellation

    /**
     * Initialize given widgets to default state, where we launch Music on default click and hide
     * actions if service not running.
     */
    override fun defaultAppWidget(context: Context, appWidgetIds: IntArray) {
        val appWidgetView = RemoteViews(context.packageName, R.layout.app_widget_circle)

        appWidgetView.setImageViewResource(R.id.image, R.drawable.default_audio_art)
        val secondaryColor = MaterialValueHelper.getSecondaryTextColor(context, true)
        appWidgetView.setImageViewBitmap(
            R.id.button_toggle_play_pause,
            context.getTintedDrawable(
                R.drawable.ic_play_arrow,
                secondaryColor
            ).toBitmap()
        )

        linkButtons(context, appWidgetView)
        pushUpdate(context, appWidgetIds, appWidgetView)
    }

    /**
     * Update all active widget instances by pushing changes
     */
    override fun performUpdate(service: MusicService, appWidgetIds: IntArray?) {
        val appWidgetView = RemoteViews(service.packageName, R.layout.app_widget_circle)

        val isPlaying = service.isPlaying
        val song = service.currentSong

        // Set correct drawable for pause state
        val playPauseRes =
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
        appWidgetView.setImageViewBitmap(
            R.id.button_toggle_play_pause,
            service.getTintedDrawable(
                playPauseRes,
                MaterialValueHelper.getSecondaryTextColor(service, true)
            ).toBitmap()
        )
        val isFavorite = runBlocking(Dispatchers.IO) {
            return@runBlocking MusicUtil.isFavorite(song)
        }
        val favoriteRes =
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        appWidgetView.setImageViewBitmap(
            R.id.button_toggle_favorite,
            service.getTintedDrawable(
                favoriteRes,
                MaterialValueHelper.getSecondaryTextColor(service, true)
            ).toBitmap()
        )

        // Link actions buttons to intents
        linkButtons(service, appWidgetView)

        if (imageSize == 0) {
            val p = RetroUtil.getScreenSize(service)
            imageSize = p.x.coerceAtMost(p.y)
        }

        // Load the album cover async and push the update on completion
        service.runOnUiThread {
            if (target != null) {
                Glide.with(service).clear(target)
            }
            target = GlideApp.with(service).asBitmapPalette().songCoverOptions(song)
                .load(RetroGlideExtension.getSongModel(song))
                .apply(RequestOptions.circleCropTransform())
                .into(object : CustomTarget<BitmapPaletteWrapper>(imageSize, imageSize) {
                    override fun onResourceReady(
                        resource: BitmapPaletteWrapper,
                        transition: Transition<in BitmapPaletteWrapper>?,
                    ) {
                        val palette = resource.palette
                        update(
                            resource.bitmap, palette.getVibrantColor(
                                palette.getMutedColor(
                                    MaterialValueHelper.getSecondaryTextColor(
                                        service, true
                                    )
                                )
                            )
                        )
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        update(null, MaterialValueHelper.getSecondaryTextColor(service, true))
                    }

                    private fun update(bitmap: Bitmap?, color: Int) {
                        // Set correct drawable for pause state
                        appWidgetView.setImageViewBitmap(
                            R.id.button_toggle_play_pause,
                            service.getTintedDrawable(
                                playPauseRes, color
                            ).toBitmap()
                        )

                        // Set favorite button drawables
                        appWidgetView.setImageViewBitmap(
                            R.id.button_toggle_favorite,
                            service.getTintedDrawable(
                                favoriteRes, color
                            ).toBitmap()
                        )
                        if (bitmap != null) {
                            appWidgetView.setImageViewBitmap(R.id.image, bitmap)
                        }

                        pushUpdate(service, appWidgetIds, appWidgetView)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }

    /**
     * Link up various button actions using [PendingIntent].
     */
    private fun linkButtons(context: Context, views: RemoteViews) {
        val action = Intent(context, MainActivity::class.java)
            .putExtra(
                MainActivity.EXPAND_PANEL,
                PreferenceUtil.isExpandPanel
            )

        val serviceName = ComponentName(context, MusicService::class.java)

        // Home
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var pendingIntent =
            PendingIntent.getActivity(
                context, 0, action, if (VersionUtils.hasMarshmallow())
                    PendingIntent.FLAG_IMMUTABLE
                else 0
            )
        views.setOnClickPendingIntent(R.id.image, pendingIntent)
        // Favorite track
        pendingIntent = buildPendingIntent(context, TOGGLE_FAVORITE, serviceName)
        views.setOnClickPendingIntent(R.id.button_toggle_favorite, pendingIntent)

        // Play and pause
        pendingIntent = buildPendingIntent(context, ACTION_TOGGLE_PAUSE, serviceName)
        views.setOnClickPendingIntent(R.id.button_toggle_play_pause, pendingIntent)
    }

    companion object {

        const val NAME = "app_widget_circle"

        private var mInstance: AppWidgetCircle? = null
        private var imageSize = 0

        val instance: AppWidgetCircle
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = AppWidgetCircle()
                }
                return mInstance!!
            }
    }
}
