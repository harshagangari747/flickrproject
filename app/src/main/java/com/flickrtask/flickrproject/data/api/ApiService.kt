package com.flickrtask.flickrproject.data.api

import com.flickrtask.flickrproject.data.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("photos_public.gne")
    suspend fun getImages(
        @Query("tags") tags: String): Response<ApiResponse>
}