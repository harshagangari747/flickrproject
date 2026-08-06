package com.flickrtask.flickrproject.data.models

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Item(
    @SerializedName("author")
    val author: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("date_taken")
    val dateTaken: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("media")
    val media: Media,
    @SerializedName("published")
    val published: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("title")
    val title: String
)