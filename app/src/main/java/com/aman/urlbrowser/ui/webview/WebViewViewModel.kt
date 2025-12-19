package com.aman.urlbrowser.ui.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewViewModel : ViewModel() {

    private val _currentUrl = MutableLiveData<String>()
    val currentUrl: LiveData<String> = _currentUrl

    private val _loadingProgress = MutableLiveData<Int>()
    val loadingProgress: LiveData<Int> = _loadingProgress

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateBack = MutableLiveData<NavigationType?>()
    val navigateBack: LiveData<NavigationType?> = _navigateBack

    fun updateCurrentUrl(url: String) {
        _currentUrl.value = url
    }

    fun updateLoadingProgress(progress: Int) {
        _loadingProgress.value = progress
        _isLoading.value = progress < 100
    }

    fun onBackButtonClicked(lastOpenedUrl: String) {
        _navigateBack.value = NavigationType.Back(lastOpenedUrl)
    }

    fun onCloseButtonClicked() {
        _navigateBack.value = NavigationType.Close
    }

    fun onNavigationComplete() {
        _navigateBack.value = null
    }
}

sealed class NavigationType {
    data class Back(val retainUrl: String) : NavigationType()
    object Close : NavigationType()
}
