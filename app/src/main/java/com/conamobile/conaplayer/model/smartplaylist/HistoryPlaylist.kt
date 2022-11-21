package com.conamobile.conaplayer.model.smartplaylist

import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.model.Song
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent

@Parcelize
class HistoryPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.history),
    iconRes = R.drawable.ic_history
), KoinComponent {

    override fun songs(): List<Song> {
        return topPlayedRepository.recentlyPlayedTracks()
    }
}