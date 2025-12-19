package com.aman.urlbrowser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UrlHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrl(urlHistory: UrlHistoryEntity)

    @Query("SELECT * FROM url_history ORDER BY timestamp DESC")
    fun getAllUrls(): LiveData<List<UrlHistoryEntity>>

    @Query("SELECT * FROM url_history ORDER BY timestamp DESC")
    suspend fun getAllUrlsList(): List<UrlHistoryEntity>

    @Query("DELETE FROM url_history")
    suspend fun deleteAllUrls()

}