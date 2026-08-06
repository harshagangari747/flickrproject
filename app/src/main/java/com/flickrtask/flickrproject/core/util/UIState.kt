package com.flickrtask.flickrproject.core.util

import com.flickrtask.flickrproject.data.models.Item

sealed class UIState {
    data class Success(val images: List<Item>) : UIState()
    data class Error(val errMsg: String) : UIState()
    object Loading : UIState()
    data class Init(val msg: String) : UIState()
}

sealed class ImageViewState {
    data class CurrentSelectedImage(val image: Item) : ImageViewState()
    data class Error(val errMsg: String) : ImageViewState()
    object Loading : ImageViewState()
}