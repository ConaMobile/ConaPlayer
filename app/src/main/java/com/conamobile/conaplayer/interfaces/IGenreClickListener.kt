package com.conamobile.conaplayer.interfaces

import android.view.View
import com.conamobile.conaplayer.model.Genre

interface IGenreClickListener {
    fun onClickGenre(genre: Genre, view: View)
}