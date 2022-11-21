package com.conamobile.conaplayer.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.conamobile.conaplayer.db.PlaylistWithSongs
import com.conamobile.conaplayer.db.SongEntity
import com.conamobile.conaplayer.repository.RealRepository

class PlaylistDetailsViewModel(
    private val realRepository: RealRepository,
    private var playlist: PlaylistWithSongs
) : ViewModel() {
    fun getSongs(): LiveData<List<SongEntity>> =
        realRepository.playlistSongs(playlist.playlistEntity.playListId)

    fun playlistExists(): LiveData<Boolean> =
        realRepository.checkPlaylistExists(playlist.playlistEntity.playListId)
}
