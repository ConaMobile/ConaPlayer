package com.conamobile.conaplayer.model.smartplaylist

import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class NotPlayedPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.not_recently_played),
    iconRes = R.drawable.ic_audiotrack
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.notRecentlyPlayedTracks()
    }
}