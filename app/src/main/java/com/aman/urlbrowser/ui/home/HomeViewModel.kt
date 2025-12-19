package com.aman.urlbrowser.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aman.urlbrowser.data.UrlRepository
import com.aman.urlbrowser.data.local.UrlHistoryDatabase
import com.aman.urlbrowser.utils.UrlValidator
import com.aman.urlbrowser.utils.ValidationResult
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UrlRepository

    private val _urlValidationResult = MutableLiveData<ValidationResult>()
    val urlValidationResult: LiveData<ValidationResult> = _urlValidationResult

    private val _navigateToWebView = MutableLiveData<String?>()
    val navigateToWebView: LiveData<String?> = _navigateToWebView

    init {
        val dao = UrlHistoryDatabase.getDatabase(application).urlHistoryDao()
        repository = UrlRepository(dao)
    }

    fun validateAndOpenUrl(inputUrl: String) {
        val result = UrlValidator.validateAndFormat(inputUrl)
        _urlValidationResult.value = result

        if (result is ValidationResult.Success) {
            saveUrlToHistory(result.formattedUrl)
            _navigateToWebView.value = result.formattedUrl
        }
    }

    private fun saveUrlToHistory(url: String) {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            repository.insertUrl(url, timestamp)
        }
    }

    fun onWebViewNavigationComplete() {
        _navigateToWebView.value = null
    }
}
