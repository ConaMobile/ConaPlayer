package com.conamobile.conaplayer.model.smartplaylist

import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class TopTracksPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.my_top_tracks),
    iconRes = R.drawable.ic_trending_up
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.topTracks()
    }
}