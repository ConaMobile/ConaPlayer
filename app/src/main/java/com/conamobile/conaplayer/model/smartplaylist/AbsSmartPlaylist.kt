package com.conamobile.conaplayer.model.smartplaylist

import androidx.annotation.DrawableRes
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.model.AbsCustomPlaylist

abstract class AbsSmartPlaylist(
    name: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_queue_music
) : AbsCustomPlaylist(
    id = PlaylistIdGenerator(name, iconRes),
    name = name
)