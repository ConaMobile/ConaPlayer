package com.conamobile.conaplayer.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.conamobile.conaplayer.EXTRA_PLAYLISTS
import com.conamobile.conaplayer.EXTRA_SONG
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.db.PlaylistEntity
import com.conamobile.conaplayer.extensions.colorButtons
import com.conamobile.conaplayer.extensions.extraNotNull
import com.conamobile.conaplayer.extensions.materialDialog
import com.conamobile.conaplayer.fragments.LibraryViewModel
import com.conamobile.conaplayer.model.Song
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddToPlaylistDialog : DialogFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {
        fun create(playlistEntities: List<PlaylistEntity>, song: Song): AddToPlaylistDialog {
            val list: MutableList<Song> = mutableListOf()
            list.add(song)
            return create(playlistEntities, list)
        }

        fun create(playlistEntities: List<PlaylistEntity>, songs: List<Song>): AddToPlaylistDialog {
            return AddToPlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_SONG to songs,
                    EXTRA_PLAYLISTS to playlistEntities
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlistEntities = extraNotNull<List<PlaylistEntity>>(EXTRA_PLAYLISTS).value
        val songs = extraNotNull<List<Song>>(EXTRA_SONG).value
        val playlistNames = mutableListOf<String>()
        playlistNames.add(requireContext().resources.getString(R.string.action_new_playlist))
        for (entity: PlaylistEntity in playlistEntities) {
            playlistNames.add(entity.playlistName)
        }
        return materialDialog(R.string.add_playlist_title)
            .setItems(playlistNames.toTypedArray()) { dialog, which->
                 if (which == 0) {
                    showCreateDialog(songs)
                } else {
                    libraryViewModel.addToPlaylist(requireContext(), playlistNames[which], songs)
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.action_cancel, null)
            .create()
            .colorButtons()
    }

    private fun showCreateDialog(songs: List<Song>) {
        CreatePlaylistDialog.create(songs).show(requireActivity().supportFragmentManager, "Dialog")
    }
}
