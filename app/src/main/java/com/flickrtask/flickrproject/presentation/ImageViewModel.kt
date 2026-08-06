package com.flickrtask.flickrproject.presentation

import android.media.Image
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flickrtask.flickrproject.core.util.ImageViewState
import com.flickrtask.flickrproject.core.util.NetworkResponse
import com.flickrtask.flickrproject.core.util.UIState
import com.flickrtask.flickrproject.domain.GetImagesUseCase
import com.flickrtask.flickrproject.core.util.stringToUrlEncodedConverter
import com.flickrtask.flickrproject.data.models.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    val getImagesUseCase: GetImagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Init("Try searching for \'whale\'"))
    val uiState = _uiState.asStateFlow()

    private val _imageViewState = MutableStateFlow<ImageViewState>(ImageViewState.Loading)
    val imageViewState = _imageViewState.asStateFlow()

    private var job: Job? = null

    @OptIn(FlowPreview::class)
    fun getImages(tags: String) {
        _uiState.value = UIState.Loading
        val urlEncodedTags = tags.stringToUrlEncodedConverter()
        Log.d("vm", "getImages: ${urlEncodedTags}")
        try {
            job?.cancel()
            job = makeApiCallToFetchImages(urlEncodedTags)
        } catch (ex: Exception) {
            Log.e("Error", "makeApiCallToFetchImages: ${ex.localizedMessage} ")
            _uiState.value =
                UIState.Error(ex.localizedMessage ?: "Failed call to remote server")
        }


    }

    private fun makeApiCallToFetchImages(urlEncodedTags: String): Job {
        Log.d("URL tags", "makeApiCallToFetchImages: $urlEncodedTags")

        val job = viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            try {
                Log.d("Waiting", "makeApiCallToFetchImages: Waiting")
                getImagesUseCase(urlEncodedTags).collect {
                    if (it is NetworkResponse.Success) {
                        _uiState.value = UIState.Success(it.apiResponse)
                    } else if (it is NetworkResponse.Loading) {
                        _uiState.value = UIState.Loading
                    } else {

                        throw Exception((it as NetworkResponse.Error).errMsg)
                    }

                }
            } catch (ex: CancellationException) {


            } catch (ex: Exception) {
                Log.e("Error", "makeApiCallToFetchImages: ${ex.message} ")
                throw Exception(ex)
            }


        }

        return job

    }

    fun currentImageSelected(item: Item) {
        try {
            _imageViewState.value = ImageViewState.CurrentSelectedImage(item)
        } catch (ex: Exception) {
            _imageViewState.value =
                ImageViewState.Error("Error loading selected Images: ${ex.localizedMessage ?: ""}")
        }

    }
}
