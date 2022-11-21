package com.conamobile.conaplayer.model.smartplaylist

import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class LastAddedPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.last_added),
    iconRes = R.drawable.ic_library_add
) {
    override fun songs(): List<Song> {
        return lastAddedRepository.recentSongs()
    }
}