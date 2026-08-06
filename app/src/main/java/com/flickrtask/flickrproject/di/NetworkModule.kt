package com.flickrtask.flickrproject.di


import com.flickrtask.flickrproject.data.QueryParamInterceptor
import com.flickrtask.flickrproject.data.RemoteRepositoryImpl
import com.flickrtask.flickrproject.data.api.ApiService
import com.flickrtask.flickrproject.domain.GetImagesUseCase
import com.flickrtask.flickrproject.domain.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
    )

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun providesOkhttpClient(
        logInterceptor: HttpLoggingInterceptor,
        queryParamInterceptor: QueryParamInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(logInterceptor)
            .addInterceptor(queryParamInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesQueryParamInterceptor(): QueryParamInterceptor {
        return QueryParamInterceptor()
    }

    @Provides
    @Singleton
    fun providesApiClient(client: OkHttpClient, gsonConverter: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.flickr.com/services/feeds/")
            .client(client)
            .addConverterFactory(gsonConverter).build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofitObj: Retrofit): ApiService {
        return retrofitObj.create(ApiService::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsRemoteRepository(repo: RemoteRepositoryImpl): RemoteRepository

}

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun providesGetImageUseCase(repo: RemoteRepository) = GetImagesUseCase(repo)
}