package com.flickrtask.flickrproject.domain

import com.flickrtask.flickrproject.core.util.NetworkResponse
import com.flickrtask.flickrproject.core.util.UIState
import com.flickrtask.flickrproject.data.models.ApiResponse
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun getImagesForTags(tags: String): Flow<NetworkResponse<ApiResponse>>
}