package com.conamobile.conaplayer.fragments.player.material

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.FragmentMaterialBinding
import com.conamobile.conaplayer.extensions.colorControlNormal
import com.conamobile.conaplayer.extensions.drawAboveSystemBars
import com.conamobile.conaplayer.extensions.surfaceColor
import com.conamobile.conaplayer.extensions.whichFragment
import com.conamobile.conaplayer.fragments.base.AbsPlayerFragment
import com.conamobile.conaplayer.fragments.player.PlayerAlbumCoverFragment
import com.conamobile.conaplayer.fragments.player.normal.PlayerFragment
import com.conamobile.conaplayer.helper.MusicPlayerRemote
import com.conamobile.conaplayer.model.Song
import com.conamobile.conaplayer.util.PreferenceUtil
import com.conamobile.conaplayer.util.ViewUtil
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor
import com.conamobile.conaplayer.views.DrawableGradient

class MaterialFragment : AbsPlayerFragment(R.layout.fragment_material) {

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    private var lastColor: Int = 0

    override val paletteColor: Int
        get() = lastColor

    private lateinit var playbackControlsFragment: MaterialControlsFragment

    private var _binding: FragmentMaterialBinding? = null
    private val binding get() = _binding!!

    private var valueAnimator: ValueAnimator? = null

    private fun colorize(i: Int) {
        if (valueAnimator != null) {
            valueAnimator?.cancel()
        }

        valueAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            surfaceColor(),
            i
        )
        valueAnimator?.addUpdateListener { animation ->
            if (isAdded) {
                val drawable = DrawableGradient(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        animation.animatedValue as Int,
                        surfaceColor()
                    ), 0
                )
                binding.colorGradientBackground.background = drawable
            }
        }
        valueAnimator?.setDuration(ViewUtil.RETRO_MUSIC_ANIM_TIME.toLong())?.start()
    }


    override fun onShow() {
        playbackControlsFragment.show()
    }

    override fun onHide() {
        playbackControlsFragment.hide()
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor() = colorControlNormal()

    override fun onColorChanged(color: MediaNotificationProcessor) {
        playbackControlsFragment.setColor(color)
        lastColor = color.backgroundColor
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

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMaterialBinding.bind(view)
        setUpSubFragments()
        setUpPlayerToolbar()
        playerToolbar().drawAboveSystemBars()
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
            setOnMenuItemClickListener(this@MaterialFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                colorControlNormal(),
                requireActivity()
            )
        }
    }

    override fun onServiceConnected() {
        updateIsFavorite()
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
    }

    companion object {

        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
