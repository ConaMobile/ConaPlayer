package com.conamobile.conaplayer.fragments.player.peek

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.FragmentPeekPlayerBinding
import com.conamobile.conaplayer.extensions.*
import com.conamobile.conaplayer.fragments.base.AbsPlayerFragment
import com.conamobile.conaplayer.fragments.base.goToAlbum
import com.conamobile.conaplayer.fragments.base.goToArtist
import com.conamobile.conaplayer.fragments.player.PlayerAlbumCoverFragment
import com.conamobile.conaplayer.helper.MusicPlayerRemote
import com.conamobile.conaplayer.util.PreferenceUtil
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor

class PeekPlayerFragment : AbsPlayerFragment(R.layout.fragment_peek_player) {

    private lateinit var controlsFragment: PeekPlayerControlFragment
    private var lastColor: Int = 0
    private var _binding: FragmentPeekPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPeekPlayerBinding.bind(view)
        setUpPlayerToolbar()
        setUpSubFragments()
        binding.title.isSelected = true
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
        binding.root.drawAboveSystemBarsWithPadding()
    }

    private fun setUpSubFragments() {
        controlsFragment =
            whichFragment(R.id.playbackControlsFragment) as PeekPlayerControlFragment

        val coverFragment =
            whichFragment(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        coverFragment.setCallbacks(this)
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener(this@PeekPlayerFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                colorControlNormal(),
                requireActivity()
            )
        }
    }

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor() = colorControlNormal()

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.primaryTextColor
        libraryViewModel.updateColor(color.primaryTextColor)
        controlsFragment.setColor(color)
    }

    override fun onFavoriteToggled() {
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.title.text = song.title
        binding.text.text = song.artistName

        if (PreferenceUtil.isSongInfo) {
            binding.songInfo.text = getSongInfo(song)
            binding.songInfo.show()
        } else {
            binding.songInfo.hide()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
