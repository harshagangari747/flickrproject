package com.flickrtask.flickrproject.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchBarScreen(
    callback: (String) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            callback(searchText)
        },
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}