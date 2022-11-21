package com.conamobile.conaplayer.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import androidx.fragment.app.DialogFragment
import com.conamobile.conaplayer.EXTRA_SONG
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.DialogFileDetailsBinding
import com.conamobile.conaplayer.extensions.colorButtons
import com.conamobile.conaplayer.extensions.materialDialog
import com.conamobile.conaplayer.model.Song
import com.conamobile.conaplayer.util.MusicUtil
import org.jaudiotagger.audio.AudioFileIO
import java.io.File

class SongDetailDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context: Context = requireContext()
        val binding = DialogFileDetailsBinding.inflate(layoutInflater)

        val song = requireArguments().getParcelable<Song>(EXTRA_SONG)
        with(binding) {
            fileName.text = makeTextWithTitle(context, R.string.label_file_name, "-")
            filePath.text = makeTextWithTitle(context, R.string.label_file_path, "-")
            fileSize.text = makeTextWithTitle(context, R.string.label_file_size, "-")
            fileFormat.text = makeTextWithTitle(context, R.string.label_file_format, "-")
            trackLength.text = makeTextWithTitle(context, R.string.label_track_length, "-")
            bitrate.text = makeTextWithTitle(context, R.string.label_bit_rate, "-")
            samplingRate.text = makeTextWithTitle(context, R.string.label_sampling_rate, "-")
        }

        if (song != null) {
            val songFile = File(song.data)
            if (songFile.exists()) {
                binding.fileName.text =
                    makeTextWithTitle(context, R.string.label_file_name, songFile.name)
                binding.filePath.text =
                    makeTextWithTitle(context, R.string.label_file_path, songFile.absolutePath)

                binding.dateModified.text = makeTextWithTitle(
                    context, R.string.label_last_modified,
                    MusicUtil.getDateModifiedString(songFile.lastModified())
                )

                binding.fileSize.text =
                    makeTextWithTitle(
                        context,
                        R.string.label_file_size,
                        getFileSizeString(songFile.length())
                    )
                try {
                    val audioFile = AudioFileIO.read(songFile)
                    val audioHeader = audioFile.audioHeader

                    binding.fileFormat.text =
                        makeTextWithTitle(context, R.string.label_file_format, audioHeader.format)
                    binding.trackLength.text = makeTextWithTitle(
                        context,
                        R.string.label_track_length,
                        MusicUtil.getReadableDurationString((audioHeader.trackLength * 1000).toLong())
                    )
                    binding.bitrate.text = makeTextWithTitle(
                        context,
                        R.string.label_bit_rate,
                        audioHeader.bitRate + " kb/s"
                    )
                    binding.samplingRate.text =
                        makeTextWithTitle(
                            context,
                            R.string.label_sampling_rate,
                            audioHeader.sampleRate + " Hz"
                        )
                } catch (e: Exception) {
                    Log.e(TAG, "error while reading the song file", e)
                    // fallback
                    binding.trackLength.text = makeTextWithTitle(
                        context,
                        R.string.label_track_length,
                        MusicUtil.getReadableDurationString(song.duration)
                    )
                }
            } else {
                // fallback
                binding.fileName.text =
                    makeTextWithTitle(context, R.string.label_file_name, song.title)
                binding.trackLength.text = makeTextWithTitle(
                    context,
                    R.string.label_track_length,
                    MusicUtil.getReadableDurationString(song.duration)
                )
            }
        }
        return materialDialog(R.string.action_details)
            .setPositiveButton(android.R.string.ok, null)
            .setView(binding.root)
            .create()
            .colorButtons()
    }

    companion object {

        val TAG: String = SongDetailDialog::class.java.simpleName

        fun create(song: Song): SongDetailDialog {
            return SongDetailDialog().apply {
                arguments = bundleOf(
                    EXTRA_SONG to song
                )
            }
        }

        private fun makeTextWithTitle(context: Context, titleResId: Int, text: String?): Spanned {
            return ("<b>" + context.resources.getString(titleResId) + ": " + "</b>" + text)
                .parseAsHtml()
        }

        private fun getFileSizeString(sizeInBytes: Long): String {
            val fileSizeInKB = sizeInBytes / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            return "$fileSizeInMB MB"
        }
    }
}
