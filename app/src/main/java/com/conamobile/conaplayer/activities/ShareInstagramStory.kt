package com.conamobile.conaplayer.activities

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import com.conamobile.appthemehelper.util.ColorUtil
import com.conamobile.appthemehelper.util.MaterialValueHelper
import com.conamobile.conaplayer.activities.base.AbsThemeActivity
import com.conamobile.conaplayer.databinding.ActivityShareInstagramBinding
import com.conamobile.conaplayer.extensions.accentColor
import com.conamobile.conaplayer.extensions.setLightStatusBar
import com.conamobile.conaplayer.extensions.setStatusBarColor
import com.conamobile.conaplayer.glide.GlideApp
import com.conamobile.conaplayer.glide.RetroGlideExtension
import com.conamobile.conaplayer.glide.RetroMusicColoredTarget
import com.conamobile.conaplayer.model.Song
import com.conamobile.conaplayer.util.Share
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor

class ShareInstagramStory : AbsThemeActivity() {

    private lateinit var binding: ActivityShareInstagramBinding

    companion object {
        const val EXTRA_SONG = "extra_song"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareInstagramBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(Color.TRANSPARENT)

        binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
        setSupportActionBar(binding.toolbar)

        val song = intent.extras?.getParcelable<Song>(EXTRA_SONG)
        song?.let { songFinal ->
            GlideApp.with(this)
                .asBitmapPalette()
                .songCoverOptions(songFinal)
                .load(RetroGlideExtension.getSongModel(songFinal))
                .into(object : RetroMusicColoredTarget(binding.image) {
                    override fun onColorReady(colors: MediaNotificationProcessor) {
                        val isColorLight = ColorUtil.isColorLight(colors.backgroundColor)
                        setColors(isColorLight, colors.backgroundColor)
                    }
                })

            binding.shareTitle.text = songFinal.title
            binding.shareText.text = songFinal.artistName
            binding.shareButton.setOnClickListener {
                val path: String = Media.insertImage(
                    contentResolver,
                    binding.mainContent.drawToBitmap(Bitmap.Config.ARGB_8888),
                    "Design", null
                )
                Share.shareStoryToSocial(
                    this@ShareInstagramStory,
                    path.toUri()
                )
            }
        }
        binding.shareButton.setTextColor(
            MaterialValueHelper.getPrimaryTextColor(
                this,
                ColorUtil.isColorLight(accentColor())
            )
        )
        binding.shareButton.backgroundTintList =
            ColorStateList.valueOf(accentColor())
    }

    private fun setColors(colorLight: Boolean, color: Int) {
        setLightStatusBar(colorLight)
        binding.toolbar.setTitleTextColor(
            MaterialValueHelper.getPrimaryTextColor(
                this@ShareInstagramStory,
                colorLight
            )
        )
        binding.toolbar.navigationIcon?.setTintList(
            ColorStateList.valueOf(
                MaterialValueHelper.getPrimaryTextColor(
                    this@ShareInstagramStory,
                    colorLight
                )
            )
        )
        binding.mainContent.background =
            GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(color, Color.BLACK)
            )
    }
}
