package com.conamobile.conaplayer.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.conamobile.conaplayer.BuildConfig
import com.conamobile.conaplayer.extensions.showToast

fun Activity.maybeShowAnnoyingToasts() {
    if (BuildConfig.APPLICATION_ID != "com.conamobile.conaplayer" &&
        BuildConfig.APPLICATION_ID != "com.conamobile.conaplayer.debug" &&
        BuildConfig.APPLICATION_ID != "com.conamobile.conaplayer.normal"
    ) {
        if (BuildConfig.DEBUG) {
            // Log these things to console, if the plagiarizer even cares to check it
            Log.d("Retro Music", "What are you doing with your life?")
            Log.d("Retro Music", "Stop copying apps and make use of your brain.")
            Log.d("Retro Music", "Stop doing this or you will end up straight to hell.")
            Log.d("Retro Music", "To the boiler room of hell. All the way down.")
        } else {
            showToast("Warning! This is a copy of Retro Music Player", Toast.LENGTH_LONG)
            showToast("Instead of using this copy by a dumb person who didn't even bother to remove this code.",
                Toast.LENGTH_LONG)
            showToast("Support us by downloading the original version from Play Store.",
                Toast.LENGTH_LONG)
            val packageName = "com.conamobile.conaplayer"
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                    "market://details?id=$packageName".toUri()))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()))
            }
        }
    }
}