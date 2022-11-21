package com.conamobile.conaplayer.dialogs

import android.app.Dialog
import android.media.MediaScannerConnection
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.conamobile.appthemehelper.util.VersionUtils
import com.conamobile.conaplayer.EXTRA_PLAYLIST
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.db.PlaylistWithSongs
import com.conamobile.conaplayer.extensions.*
import com.conamobile.conaplayer.helper.M3UWriter
import com.conamobile.conaplayer.util.PlaylistsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavePlaylistDialog : DialogFragment() {
    companion object {
        fun create(playlistWithSongs: PlaylistWithSongs): SavePlaylistDialog {
            return SavePlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_PLAYLIST to playlistWithSongs
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playlistWithSongs = extraNotNull<PlaylistWithSongs>(EXTRA_PLAYLIST).value

        if (VersionUtils.hasR()) {
            createNewFile(
                "audio/mpegurl",
                playlistWithSongs.playlistEntity.playlistName
            ) { outputStream, data ->
                try {
                    if (outputStream != null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            M3UWriter.writeIO(
                                outputStream,
                                playlistWithSongs
                            )
                            withContext(Dispatchers.Main) {
                                showToast(
                                    requireContext().getString(R.string.saved_playlist_to,
                                        data?.lastPathSegment),
                                    Toast.LENGTH_LONG
                                )
                                dismiss()
                            }
                        }
                    }
                } catch (e: Exception) {
                    showToast(
                        "Something went wrong : " + e.message
                    )
                }
            }
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val file = PlaylistsUtil.savePlaylistWithSongs(playlistWithSongs)
                MediaScannerConnection.scanFile(
                    requireActivity(),
                    arrayOf<String>(file.path),
                    null
                ) { _, _ ->
                }
                withContext(Dispatchers.Main) {
                    showToast(
                        getString(R.string.saved_playlist_to, file),
                        Toast.LENGTH_LONG
                    )
                    dismiss()
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog(R.string.save_playlist_title)
            .setView(R.layout.loading)
            .create().colorButtons()
    }
}
