package com.flickrtask.flickrproject.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class QueryParamInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1").build()

        val newRequest = originalRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}