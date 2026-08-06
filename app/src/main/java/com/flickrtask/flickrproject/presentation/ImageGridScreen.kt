package com.flickrtask.flickrproject.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.flickrtask.flickrproject.data.models.Item
import org.jetbrains.annotations.Async

@Composable
fun ImageGridScreen(images: List<Item>, callback: (item:Item)-> Unit) {
    val state = rememberLazyGridState()
    val cells = GridCells.Fixed(2)
    LazyVerticalGrid(columns = cells, Modifier, state, userScrollEnabled = true) {
        items(images, key = { it.link }) {
            AsyncImage(
                model = it.media.m,
                contentDescription = it.title,
                modifier = Modifier.size(120.dp)
                    .padding(8.dp).clickable(
                        enabled = true,
                        onClick = {
                            callback(it)
                        }
                    )
            )


        }


    }
}