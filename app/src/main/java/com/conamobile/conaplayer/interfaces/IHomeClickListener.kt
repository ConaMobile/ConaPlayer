package com.conamobile.conaplayer.interfaces

import com.conamobile.conaplayer.model.Album
import com.conamobile.conaplayer.model.Artist
import com.conamobile.conaplayer.model.Genre

interface IHomeClickListener {
    fun onAlbumClick(album: Album)

    fun onArtistClick(artist: Artist)

    fun onGenreClick(genre: Genre)
}