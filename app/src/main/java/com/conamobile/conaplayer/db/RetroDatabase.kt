package com.conamobile.conaplayer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.conamobile.conaplayer.db.HistoryDao
import com.conamobile.conaplayer.db.HistoryEntity
import com.conamobile.conaplayer.db.PlayCountDao
import com.conamobile.conaplayer.db.PlayCountEntity

@Database(
    entities = [PlaylistEntity::class, SongEntity::class, HistoryEntity::class, PlayCountEntity::class],
    version = 24,
    exportSchema = false
)
abstract class RetroDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun playCountDao(): PlayCountDao
    abstract fun historyDao(): HistoryDao
}
