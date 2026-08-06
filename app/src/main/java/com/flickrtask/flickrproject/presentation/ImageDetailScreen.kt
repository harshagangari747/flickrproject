package com.flickrtask.flickrproject.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.flickrtask.flickrproject.data.models.Item
import com.flickrtask.flickrproject.data.models.Media
import com.flickrtask.flickrproject.ui.theme.FlickrProjectTheme
import okhttp3.internal.format
import java.sql.Date
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageDetailScreen(
    item: Item,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.media.m,
            contentDescription = item.description,
            Modifier.size(250.dp)
        )

        ImageMetaDataItem("Title", item.title)
        ImageMetaDataItem("Author", item.author)
        ImageMetaDataItem("Description", item.description)

        val datePattern = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val date = ZonedDateTime.parse(item.dateTaken)
        val formattedDate = date.format(datePattern)

        ImageMetaDataItem(
            "Date",
            formattedDate
        )

    }
}


@Composable
fun ImageMetaDataItem(key: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(key, fontWeight = FontWeight.Bold)
        Text(value)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ImageDetailScreenPreview() {

    val item = Item(
        title = "1D0E8568",
        link = "https://www.flickr.com/photos/186325776@N02/55432920528/",
        media = Media(m = "https://live.staticflickr.com/65535/55432920528_9ef95c2c3b_m.jpg"),
        dateTaken = "2026-06-25T07:32:25-08:00",
        description = " <p><a href=\"https://www.flickr.com/people/186325776@N02/\">guy.baechler</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/186325776@N02/55432920528/\" title=\"1D0E8568\"><img src=\"https://live.staticflickr.com/65535/55432920528_9ef95c2c3b_m.jpg\" width=\"240\" height=\"160\" alt=\"1D0E8568\"/></a></p> ",
        published = "2026-07-31T12:49:36Z",
        author = "nobody@flickr.com (\"guy.baechler\")",
        authorId = "186325776@N02",
        tags = "2026 servion lion"
    )

    FlickrProjectTheme() {
        ImageDetailScreen(
            item
        )
    }
}