package com.aman.urlbrowser.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aman.urlbrowser.data.UrlRepository
import com.aman.urlbrowser.data.local.UrlHistoryDatabase
import com.aman.urlbrowser.data.local.UrlHistoryEntity
import com.aman.urlbrowser.data.remote.HistoryUploadRequest
import com.aman.urlbrowser.data.remote.RetrofitClient
import com.aman.urlbrowser.data.remote.UrlItem
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UrlRepository

    val allUrls: LiveData<List<UrlHistoryEntity>>

    private val _uploadStatus = MutableLiveData<UploadStatus>()
    val uploadStatus: LiveData<UploadStatus> = _uploadStatus

    private val _clearStatus = MutableLiveData<Boolean>()
    val clearStatus: LiveData<Boolean> = _clearStatus

    init {
        val dao = UrlHistoryDatabase.getDatabase(application).urlHistoryDao()
        repository = UrlRepository(dao)
        allUrls = repository.allUrls
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            try {
                repository.clearAllUrls()
                _clearStatus.value = true
            } catch (e: Exception) {
                _clearStatus.value = false
            }
        }
    }

    fun uploadHistory() {
        viewModelScope.launch {
            try {
                _uploadStatus.value = UploadStatus.Loading

                val urlList = repository.getAllUrlsList()

                if (urlList.isEmpty()) {
                    _uploadStatus.value = UploadStatus.Error("No history to upload")
                    return@launch
                }

                val urlItems = urlList.map { UrlItem(it.url, it.timestamp) }
                val request = HistoryUploadRequest(urlItems)

                val response = RetrofitClient.apiService.uploadHistory(request)

                if (response.isSuccessful) {
                    _uploadStatus.value = UploadStatus.Success("History uploaded successfully")
                } else {
                    _uploadStatus.value = UploadStatus.Error("Upload failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _uploadStatus.value = UploadStatus.Error("Upload failed: ${e.message}")
            }
        }
    }

    fun resetUploadStatus() {
        _uploadStatus.value = UploadStatus.Idle
    }
}

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    data class Success(val message: String) : UploadStatus()
    data class Error(val message: String) : UploadStatus()
}
