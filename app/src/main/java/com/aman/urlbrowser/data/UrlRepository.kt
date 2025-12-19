package com.aman.urlbrowser.data

import androidx.lifecycle.LiveData
import com.aman.urlbrowser.data.local.UrlHistoryDao
import com.aman.urlbrowser.data.local.UrlHistoryEntity

class UrlRepository(private val urlHistoryDao: UrlHistoryDao) {
    val allUrls: LiveData<List<UrlHistoryEntity>> = urlHistoryDao.getAllUrls()

    suspend fun insertUrl(url: String, timestamp: Long) {
        val urlHistory = UrlHistoryEntity(url = url, timestamp = timestamp)

        urlHistoryDao.insertUrl(urlHistory)
    }

    suspend fun getAllUrlsList(): List<UrlHistoryEntity> {
        return urlHistoryDao.getAllUrlsList()
    }

    suspend fun clearAllUrls() {
        urlHistoryDao.deleteAllUrls()
    }

}