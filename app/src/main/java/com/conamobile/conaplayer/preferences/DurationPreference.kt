package com.conamobile.conaplayer.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.fragment.app.DialogFragment
import com.conamobile.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.PreferenceDialogAudioFadeBinding
import com.conamobile.conaplayer.extensions.addAccentColor
import com.conamobile.conaplayer.extensions.colorButtons
import com.conamobile.conaplayer.extensions.colorControlNormal
import com.conamobile.conaplayer.extensions.materialDialog
import com.conamobile.conaplayer.util.PreferenceUtil
import com.google.android.material.slider.Slider


class DurationPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {
    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }
}

class DurationPreferenceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = PreferenceDialogAudioFadeBinding.inflate(layoutInflater)

        binding.slider.apply {
            addAccentColor()
            value = PreferenceUtil.audioFadeDuration.toFloat()
            updateText(value.toInt(), binding.duration)
            addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    updateText(value.toInt(), binding.duration)
                }
            })
        }


        return materialDialog(R.string.audio_fade_duration)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.save) { _, _ -> updateDuration(binding.slider.value.toInt()) }
            .setView(binding.root)
            .create()
            .colorButtons()
    }

    private fun updateText(value: Int, duration: TextView) {
        var durationText = "$value ms"
        if (value == 0) durationText += " / Off"
        duration.text = durationText
    }

    private fun updateDuration(duration: Int) {
        PreferenceUtil.audioFadeDuration = duration
    }

    companion object {
        fun newInstance(): DurationPreferenceDialog {
            return DurationPreferenceDialog()
        }
    }
}