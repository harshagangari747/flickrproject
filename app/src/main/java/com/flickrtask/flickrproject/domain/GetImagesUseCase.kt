package com.flickrtask.flickrproject.domain

import com.flickrtask.flickrproject.core.util.NetworkResponse
import com.flickrtask.flickrproject.data.models.ApiResponse
import com.flickrtask.flickrproject.data.models.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(val repo: RemoteRepository) {
    suspend operator fun invoke(tags: String): Flow<NetworkResponse<List<Item>>> {
        if (tags.isBlank()) {
            return flow {
                emit(NetworkResponse.Error("Image tag can't be blank"))
            }
        }
        return repo.getImagesForTags(tags).map {
            when (it) {
               is NetworkResponse.Success -> {
                    NetworkResponse.Success(it.apiResponse.items)
                }

                is NetworkResponse.Error -> {
                    it
                }

                is NetworkResponse.Loading -> {
                    it
                }

            }

        }
    }
}