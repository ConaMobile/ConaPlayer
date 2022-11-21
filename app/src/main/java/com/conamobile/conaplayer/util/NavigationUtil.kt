package com.conamobile.conaplayer.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.widget.Toast
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.activities.DriveModeActivity
import com.conamobile.conaplayer.extensions.showToast
import com.conamobile.conaplayer.helper.MusicPlayerRemote.audioSessionId

object NavigationUtil {
    fun gotoDriveMode(activity: Activity) {
        activity.startActivity(
            Intent(activity, DriveModeActivity::class.java), null
        )
    }

    fun openEqualizer(activity: Activity) {
        stockEqualizer(activity)
    }

    private fun stockEqualizer(activity: Activity) {
        val sessionId = audioSessionId
        if (sessionId == AudioEffect.ERROR_BAD_VALUE) {
            activity.showToast(R.string.no_audio_ID, Toast.LENGTH_LONG)
        } else {
            try {
                val effects = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
                effects.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                activity.startActivityForResult(effects, 0)
            } catch (notFound: ActivityNotFoundException) {
                activity.showToast(R.string.no_equalizer)
            }
        }
    }
}