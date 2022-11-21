package com.conamobile.conaplayer

import androidx.room.Room
import com.conamobile.conaplayer.cast.RetroWebServer
import com.conamobile.conaplayer.auto.AutoMusicProvider
import com.conamobile.conaplayer.db.MIGRATION_23_24
import com.conamobile.conaplayer.db.PlaylistWithSongs
import com.conamobile.conaplayer.db.RetroDatabase
import com.conamobile.conaplayer.fragments.LibraryViewModel
import com.conamobile.conaplayer.fragments.albums.AlbumDetailsViewModel
import com.conamobile.conaplayer.fragments.artists.ArtistDetailsViewModel
import com.conamobile.conaplayer.fragments.genres.GenreDetailsViewModel
import com.conamobile.conaplayer.fragments.playlists.PlaylistDetailsViewModel
import com.conamobile.conaplayer.model.Genre
import com.conamobile.conaplayer.network.provideDefaultCache
import com.conamobile.conaplayer.network.provideLastFmRest
import com.conamobile.conaplayer.network.provideLastFmRetrofit
import com.conamobile.conaplayer.network.provideOkHttp
import com.conamobile.conaplayer.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {

    factory {
        provideDefaultCache()
    }
    factory {
        provideOkHttp(get(), get())
    }
    single {
        provideLastFmRetrofit(get())
    }
    single {
        provideLastFmRest(get())
    }
}

private val roomModule = module {

    single {
        Room.databaseBuilder(androidContext(), RetroDatabase::class.java, "playlist.db")
            .addMigrations(MIGRATION_23_24)
            .build()
    }

    factory {
        get<RetroDatabase>().playlistDao()
    }

    factory {
        get<RetroDatabase>().playCountDao()
    }

    factory {
        get<RetroDatabase>().historyDao()
    }

    single {
        RealRoomRepository(get(), get(), get())
    } bind RoomRepository::class
}
private val autoModule = module {
    single {
        AutoMusicProvider(
            androidContext(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
private val mainModule = module {
    single {
        androidContext().contentResolver
    }
    single {
        RetroWebServer(get())
    }
}
private val dataModule = module {
    single {
        RealRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    } bind Repository::class

    single {
        RealSongRepository(get())
    } bind SongRepository::class

    single {
        RealGenreRepository(get(), get())
    } bind GenreRepository::class

    single {
        RealAlbumRepository(get())
    } bind AlbumRepository::class

    single {
        RealArtistRepository(get(), get())
    } bind ArtistRepository::class

    single {
        RealPlaylistRepository(get())
    } bind PlaylistRepository::class

    single {
        RealTopPlayedRepository(get(), get(), get(), get())
    } bind TopPlayedRepository::class

    single {
        RealLastAddedRepository(
            get(),
            get(),
            get()
        )
    } bind LastAddedRepository::class

    single {
        RealSearchRepository(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        RealLocalDataRepository(get())
    } bind LocalDataRepository::class
}

private val viewModules = module {

    viewModel {
        LibraryViewModel(get())
    }

    viewModel { (albumId: Long) ->
        AlbumDetailsViewModel(
            get(),
            albumId
        )
    }

    viewModel { (artistId: Long?, artistName: String?) ->
        ArtistDetailsViewModel(
            get(),
            artistId,
            artistName
        )
    }

    viewModel { (playlist: PlaylistWithSongs) ->
        PlaylistDetailsViewModel(
            get(),
            playlist
        )
    }

    viewModel { (genre: Genre) ->
        GenreDetailsViewModel(
            get(),
            genre
        )
    }
}

val appModules = listOf(mainModule, dataModule, autoModule, viewModules, networkModule, roomModule)