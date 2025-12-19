package com.aman.urlbrowser.utils

import android.util.Patterns
import android.webkit.URLUtil

object UrlValidator {

    fun isValidUrl(url: String): Boolean {
        val trimmedUrl = url.trim()

        if (trimmedUrl.isEmpty()) {
            return false
        }

        // Check if it's a valid URL pattern
        return Patterns.WEB_URL.matcher(trimmedUrl).matches() ||
                URLUtil.isValidUrl(trimmedUrl)
    }

    fun formatUrl(url: String): String {
        var trimmedUrl = url.trim()

        // If URL doesn't have a scheme, prepend https://
        if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            trimmedUrl = "https://$trimmedUrl"
        }

        return trimmedUrl
    }

    fun validateAndFormat(url: String): ValidationResult {
        val trimmedUrl = url.trim()

        return when {
            trimmedUrl.isEmpty() -> {
                ValidationResult.Error("Please enter a URL")
            }

            !isValidUrl(trimmedUrl) -> {
                ValidationResult.Error("Please enter a valid URL")
            }

            else -> {
                ValidationResult.Success(formatUrl(trimmedUrl))
            }
        }
    }
}

sealed class ValidationResult {
    data class Success(val formattedUrl: String) : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
