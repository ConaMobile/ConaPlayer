package com.conamobile.conaplayer.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.extensions.colorButtons
import com.conamobile.conaplayer.extensions.materialDialog
import com.conamobile.conaplayer.fragments.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImportPlaylistDialog : DialogFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog(R.string.import_playlist)
            .setMessage(R.string.import_playlist_message)
            .setPositiveButton(R.string.import_label) { _, _ ->
                try {
                    libraryViewModel.importPlaylists()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .create()
            .colorButtons()
    }
}
