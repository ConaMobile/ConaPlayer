package com.conamobile.conaplayer.volume

interface OnAudioVolumeChangedListener {
    fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int)
}