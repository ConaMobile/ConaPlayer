package com.conamobile.conaplayer.model

import androidx.annotation.StringRes
import com.conamobile.conaplayer.HomeSection

data class Home(
    val arrayList: List<Any>,
    @HomeSection
    val homeSection: Int,
    @StringRes
    val titleRes: Int
)