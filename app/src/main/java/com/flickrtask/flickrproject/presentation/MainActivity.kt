package com.flickrtask.flickrproject.presentation

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.transition.CircularPropagation
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flickrtask.flickrproject.core.util.UIState
import com.flickrtask.flickrproject.data.RemoteRepositoryImpl
import com.flickrtask.flickrproject.data.api.ApiService
import com.flickrtask.flickrproject.domain.GetImagesUseCase
import com.flickrtask.flickrproject.ui.theme.FlickrProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flickrtask.flickrproject.core.util.ImageViewState
import com.flickrtask.flickrproject.data.models.Item

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ImageViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
            FlickrProjectTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp)
                ) { innerPadding ->
                    viewModel = hiltViewModel()
                    SearchBarScreen {
                        Log.d("TAG", "$it")
                        viewModel.getImages(it)
                    }
                    HomeScreen(viewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: ImageViewModel) { val state by viewModel.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "/home"
    ) {
        composable("/home") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val currState = state) {
                    is UIState.Init -> {
                        Text(currState.msg)
                    }

                    is UIState.Loading -> {

                        CircularProgressIndicator()
                    }

                    is UIState.Error -> {
                        Text(
                            "Error occured. ${currState.errMsg}",
                            Modifier.padding(horizontal = 20.dp),
                            color = Color.Red
                        )
                    }

                    is UIState.Success -> {
                        Column(Modifier.padding(top = 0.dp)) {
                            if (currState.images.size <= 0) {
                                Text("Search fetched 0 images :(")
                            } else {
                                ImageGridScreen(currState.images) {
                                    Log.d("TAG", "HomeScreen: ${it.toString()}")
                                    viewModel.currentImageSelected(it)
                                    navController.navigate("/imageview")
                                }
                            }
                        }

                    }

                }
            }
        }

        composable("/imageview") {
            val imageState by viewModel.imageViewState.collectAsStateWithLifecycle()

            when (val currImageState = imageState) {
                is ImageViewState.Loading -> {
                    CircularProgressIndicator()
                }

                is ImageViewState.CurrentSelectedImage -> {
                    ImageDetailScreen(currImageState.image)

                }

                is ImageViewState.Error -> {
                    Text("Error showing selected Image")

                }

            }

        }
    }


}


