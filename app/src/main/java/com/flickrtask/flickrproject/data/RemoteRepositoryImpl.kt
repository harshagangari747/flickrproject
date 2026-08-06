package com.flickrtask.flickrproject.data

import android.util.Log
import com.flickrtask.flickrproject.core.util.NetworkResponse
import com.flickrtask.flickrproject.data.api.ApiService
import com.flickrtask.flickrproject.data.models.ApiResponse
import com.flickrtask.flickrproject.domain.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(val apiService: ApiService) : RemoteRepository {
    override suspend fun getImagesForTags(tags: String): Flow<NetworkResponse<ApiResponse>> = flow {
        emit(NetworkResponse.Loading)
        try {
            Log.d("repo", "Calling ${tags}")
            val result = apiService.getImages(tags)
            if (result.isSuccessful && result.body() != null) {
                emit(NetworkResponse.Success(result.body()!!))
            } else {
                val errMsg = result.errorBody()
                emit(NetworkResponse.Error("Api request failed : $errMsg"))
            }

        } catch (ex: IOException) {
            emit(NetworkResponse.Error("Error connecting to server. Probably Internet Error: ${ex.localizedMessage ?: "Please try again later"}"))
        } catch (ex: Exception) {
            emit(
                NetworkResponse.Error("Error occured during network call: ${ex.localizedMessage ?: "Please try later"}")
            )
        }

    }


}