package com.conamobile.conaplayer.interfaces

import android.view.View
import com.conamobile.conaplayer.db.PlaylistWithSongs

interface IPlaylistClickListener {
    fun onPlaylistClick(playlistWithSongs: PlaylistWithSongs, view: View)
}