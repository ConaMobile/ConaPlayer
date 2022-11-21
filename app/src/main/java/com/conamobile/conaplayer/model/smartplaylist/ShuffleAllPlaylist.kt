package com.conamobile.conaplayer.model.smartplaylist

import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class ShuffleAllPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.action_shuffle_all),
    iconRes = R.drawable.ic_shuffle
) {
    override fun songs(): List<Song> {
        return songRepository.songs()
    }
}