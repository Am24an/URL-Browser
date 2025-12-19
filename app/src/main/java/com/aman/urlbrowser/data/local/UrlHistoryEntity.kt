package com.aman.urlbrowser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_history")
data class UrlHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val timestamp: Long
)
