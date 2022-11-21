package com.conamobile.conaplayer.fragments.player.blur

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper
import com.conamobile.conaplayer.NEW_BLUR_AMOUNT
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.FragmentBlurBinding
import com.conamobile.conaplayer.extensions.drawAboveSystemBars
import com.conamobile.conaplayer.extensions.whichFragment
import com.conamobile.conaplayer.fragments.base.AbsPlayerFragment
import com.conamobile.conaplayer.fragments.player.PlayerAlbumCoverFragment
import com.conamobile.conaplayer.glide.*
import com.conamobile.conaplayer.helper.MusicPlayerRemote
import com.conamobile.conaplayer.model.Song
import com.conamobile.conaplayer.util.PreferenceUtil.blurAmount
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor


class BlurPlayerFragment : AbsPlayerFragment(R.layout.fragment_blur),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var lastRequest: GlideRequest<Drawable>? = null

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    private lateinit var playbackControlsFragment: BlurPlaybackControlsFragment

    private var lastColor: Int = 0

    private var _binding: FragmentBlurBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBlurBinding.bind(view)
        setUpSubFragments()
        setUpPlayerToolbar()
        binding.playerToolbar.drawAboveSystemBars()
    }

    private fun setUpSubFragments() {
        playbackControlsFragment = whichFragment(R.id.playbackControlsFragment)
        val playerAlbumCoverFragment: PlayerAlbumCoverFragment =
            whichFragment(R.id.playerAlbumCoverFragment)
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            ToolbarContentTintHelper.colorizeToolbar(this, Color.WHITE, activity)
        }.setOnMenuItemClickListener(this)
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        playbackControlsFragment.setColor(color)
        lastColor = color.backgroundColor
        libraryViewModel.updateColor(color.backgroundColor)
        ToolbarContentTintHelper.colorizeToolbar(binding.playerToolbar, Color.WHITE, activity)
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override val paletteColor: Int
        get() = lastColor

    private fun updateBlur() {
        // https://github.com/bumptech/glide/issues/527#issuecomment-148840717
        GlideApp.with(this)
            .load(RetroGlideExtension.getSongModel(MusicPlayerRemote.currentSong))
            .simpleSongCoverOptions(MusicPlayerRemote.currentSong)
            .transform(
                BlurTransformation.Builder(requireContext()).blurRadius(blurAmount.toFloat())
                    .build()
            ).thumbnail(lastRequest)
            .error(GlideApp.with(this).load(ColorDrawable(Color.DKGRAY)).fitCenter())
            .also {
                lastRequest = it.clone()
                it.crossfadeListener()
                    .into(binding.colorBackground)
            }
    }

    override fun onServiceConnected() {
        updateIsFavorite()
        updateBlur()
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
        updateBlur()
    }

    override fun onPause() {
        super.onPause()
        lastRequest = null
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
        _binding = null
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == NEW_BLUR_AMOUNT) {
            updateBlur()
        }
    }
}
