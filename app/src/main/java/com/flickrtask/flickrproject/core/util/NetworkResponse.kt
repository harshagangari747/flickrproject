package com.flickrtask.flickrproject.core.util

import com.flickrtask.flickrproject.data.models.ApiResponse
import com.flickrtask.flickrproject.data.models.Item

sealed class NetworkResponse<out T> {
    data class Success<T>(val apiResponse: T) : NetworkResponse<T>()
    data class Error(val errMsg: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}