package com.conamobile.conaplayer.fragments.player.flat

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.conamobile.appthemehelper.util.ColorUtil
import com.conamobile.appthemehelper.util.MaterialValueHelper
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.FragmentFlatPlayerBinding
import com.conamobile.conaplayer.extensions.colorControlNormal
import com.conamobile.conaplayer.extensions.drawAboveSystemBars
import com.conamobile.conaplayer.extensions.whichFragment
import com.conamobile.conaplayer.fragments.base.AbsPlayerFragment
import com.conamobile.conaplayer.fragments.player.PlayerAlbumCoverFragment
import com.conamobile.conaplayer.helper.MusicPlayerRemote
import com.conamobile.conaplayer.model.Song
import com.conamobile.conaplayer.util.PreferenceUtil
import com.conamobile.conaplayer.util.ViewUtil
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor
import com.conamobile.conaplayer.views.DrawableGradient

class FlatPlayerFragment : AbsPlayerFragment(R.layout.fragment_flat_player) {
    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    private var valueAnimator: ValueAnimator? = null
    private lateinit var controlsFragment: FlatPlaybackControlsFragment
    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor

    private var _binding: FragmentFlatPlayerBinding? = null
    private val binding get() = _binding!!


    private fun setUpSubFragments() {
        controlsFragment = whichFragment(R.id.playbackControlsFragment)
        val playerAlbumCoverFragment: PlayerAlbumCoverFragment =
            whichFragment(R.id.playerAlbumCoverFragment)
        playerAlbumCoverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.inflateMenu(R.menu.menu_player)
        binding.playerToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.playerToolbar.setOnMenuItemClickListener(this)
        ToolbarContentTintHelper.colorizeToolbar(
            binding.playerToolbar,
            colorControlNormal(),
            requireActivity()
        )
    }

    private fun colorize(i: Int) {
        if (valueAnimator != null) {
            valueAnimator?.cancel()
        }

        valueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), android.R.color.transparent, i)
        valueAnimator?.addUpdateListener { animation ->
            val drawable = DrawableGradient(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(animation.animatedValue as Int, android.R.color.transparent), 0
            )
            _binding?.colorGradientBackground?.background = drawable
        }
        valueAnimator?.setDuration(ViewUtil.RETRO_MUSIC_ANIM_TIME.toLong())?.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFlatPlayerBinding.bind(view)
        setUpPlayerToolbar()
        setUpSubFragments()
        binding.playerToolbar.drawAboveSystemBars()
    }

    override fun onShow() {
        controlsFragment.show()
    }

    override fun onHide() {
        controlsFragment.hide()
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        val isLight = ColorUtil.isColorLight(paletteColor)
        return if (PreferenceUtil.isAdaptiveColor)
            MaterialValueHelper.getPrimaryTextColor(requireContext(), isLight)
        else
            colorControlNormal()
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        controlsFragment.setColor(color)
        libraryViewModel.updateColor(color.backgroundColor)
        ToolbarContentTintHelper.colorizeToolbar(
            binding.playerToolbar,
            colorControlNormal(),
            requireActivity()
        )
        if (PreferenceUtil.isAdaptiveColor) {
            colorize(color.backgroundColor)
        }
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
