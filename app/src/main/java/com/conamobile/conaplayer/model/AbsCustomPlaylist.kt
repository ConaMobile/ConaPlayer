package com.conamobile.conaplayer.model

import com.conamobile.conaplayer.repository.LastAddedRepository
import com.conamobile.conaplayer.repository.SongRepository
import com.conamobile.conaplayer.repository.TopPlayedRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class AbsCustomPlaylist(
    id: Long,
    name: String
) : Playlist(id, name), KoinComponent {

    abstract fun songs(): List<Song>

    protected val songRepository by inject<SongRepository>()

    protected val topPlayedRepository by inject<TopPlayedRepository>()

    protected val lastAddedRepository by inject<LastAddedRepository>()
}