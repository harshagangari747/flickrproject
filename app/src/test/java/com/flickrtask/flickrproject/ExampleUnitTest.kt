package com.flickrtask.flickrproject

import android.util.Log
import com.flickrtask.flickrproject.core.util.ImageViewState
import com.flickrtask.flickrproject.core.util.NetworkResponse
import com.flickrtask.flickrproject.core.util.UIState
import com.flickrtask.flickrproject.data.RemoteRepositoryImpl
import com.flickrtask.flickrproject.data.models.Item
import com.flickrtask.flickrproject.domain.GetImagesUseCase
import com.flickrtask.flickrproject.presentation.ImageViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ImageViewModelTest {

    private val repo = mockk<RemoteRepositoryImpl>()
    private val getImagesUsecasMock = mockk<GetImagesUseCase>()
    private lateinit var viewModel: ImageViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel = ImageViewModel(getImagesUsecasMock)

    }

    @Test
    fun `get_images_returns_successfull_data`() = runTest {

        //Arrange
        val item = mockk<Item>()
        val items = listOf(item, item, item)

        coEvery {
            getImagesUsecasMock("whale")
        } returns flowOf(
            NetworkResponse.Success(items)
        )


        //Act
        viewModel.getImages("whale")


        //Assert
        assertEquals(UIState.Loading, viewModel.uiState.value)

        val timeout = System.currentTimeMillis() + 2000

        while (
            viewModel.uiState.value !is UIState.Success &&
            System.currentTimeMillis() < timeout
        ) {
            Thread.sleep(50)
        }


        assertEquals(UIState.Success(items), viewModel.uiState.value)


        val itemsSize = (viewModel.uiState.value as UIState.Success).images.size

        assertEquals(itemsSize, 3)


        coVerify(exactly = 1) { getImagesUsecasMock("whale") }


    }

    @Test
    fun `get_images_returns_error`() = runTest {

        val errorMsg = "network error"

        //Arrange

        coEvery {
            getImagesUsecasMock("whale")
        } throws Exception()


        //Act
        viewModel.getImages("whale")

        //Assert
        assertEquals(UIState.Loading, viewModel.uiState.value)

        val timeout = System.currentTimeMillis() + 2000

        while (
            viewModel.uiState.value !is UIState.Success &&
            System.currentTimeMillis() < timeout
        ) {
            Thread.sleep(50)
        }


        assertEquals(UIState.Error("Something went wrong"), viewModel.uiState.value)


    }

    @Test
    fun `current_image_selected_sets_correctly`() = runTest {

        //Arrange
        val currItem = mockk<Item>()

        //Act
        assertEquals(ImageViewState.Loading, viewModel.imageViewState.value)
        viewModel.currentImageSelected(currItem)

        //Assert
        assertEquals(ImageViewState.CurrentSelectedImage(currItem), viewModel.imageViewState.value)
    }


//    @Test
//    fun `current_image_selected_throws_error`()= runTest {
//
//        //Arrange
//        val currItem = mockk<Item>()
//        val errMsg = "Can't set err msg"
//
//        //Act
//        assertEquals(ImageViewState.Loading, viewModel.imageViewState.value)
//        viewModel.currentImageSelected(currItem)
//
//        //Assert
//
//        assertEquals(ImageViewState.Error(errMsg), viewModel.imageViewState.value)
//
//
//    }

}