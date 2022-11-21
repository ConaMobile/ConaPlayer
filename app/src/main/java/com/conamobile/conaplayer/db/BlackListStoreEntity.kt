package com.conamobile.conaplayer.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BlackListStoreEntity(
    @PrimaryKey
    val path: String
)
