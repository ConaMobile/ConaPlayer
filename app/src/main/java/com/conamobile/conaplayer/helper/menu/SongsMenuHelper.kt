package com.conamobile.conaplayer.helper.menu

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.dialogs.AddToPlaylistDialog
import com.conamobile.conaplayer.dialogs.DeleteSongsDialog
import com.conamobile.conaplayer.helper.MusicPlayerRemote
import com.conamobile.conaplayer.model.Song
import com.conamobile.conaplayer.repository.RealRepository
import com.conamobile.conaplayer.util.MusicUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object SongsMenuHelper : KoinComponent {
    fun handleMenuClick(
        activity: FragmentActivity,
        songs: List<Song>,
        menuItemId: Int
    ): Boolean {
        when (menuItemId) {
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(songs)
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(songs)
                return true
            }
            R.id.action_add_to_playlist -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val playlists = get<RealRepository>().fetchPlaylists()
                    withContext(Dispatchers.Main) {
                        AddToPlaylistDialog.create(playlists, songs)
                            .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                    }
                }
                return true
            }
            R.id.action_share -> {
                activity.startActivity(
                    Intent.createChooser(
                        MusicUtil.createShareMultipleSongIntent(activity, songs),
                        null
                    )
                )
                return true
            }
            R.id.action_delete_from_device -> {
                DeleteSongsDialog.create(songs)
                    .show(activity.supportFragmentManager, "DELETE_SONGS")
                return true
            }
        }
        return false
    }
}
