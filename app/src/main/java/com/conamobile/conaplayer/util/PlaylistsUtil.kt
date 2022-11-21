package com.conamobile.conaplayer.util

import com.conamobile.conaplayer.db.PlaylistWithSongs
import com.conamobile.conaplayer.helper.M3UWriter.writeIO
import java.io.File
import java.io.IOException

object PlaylistsUtil {
    @Throws(IOException::class)
    fun savePlaylistWithSongs(playlist: PlaylistWithSongs?): File {
        return writeIO(
            File(getExternalStorageDirectory(), "Playlists"), playlist!!
        )
    }
}