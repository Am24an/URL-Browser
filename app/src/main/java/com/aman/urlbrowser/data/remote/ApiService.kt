package com.aman.urlbrowser.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("upload-history")
    suspend fun uploadHistory(@Body historyData: HistoryUploadRequest): Response<HistoryUploadResponse>
}

data class HistoryUploadRequest(
    val urls: List<UrlItem>
)

data class UrlItem(
    val url: String, val timestamp: Long
)

data class HistoryUploadResponse(
    val success: Boolean, val message: String
)
