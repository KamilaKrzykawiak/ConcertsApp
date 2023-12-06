package com.example.concertsapp.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.concertsapp.models.AttractionImage
import com.example.concertsapp.modelviews.AttractionImageModelView
import com.example.concertsapp.ui.theme.ConcertsAppTheme


class ImagesGrid : ComponentActivity() {
    lateinit var attractionImageModelView: AttractionImageModelView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            setContent {
            ConcertsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    attractionImageModelView = AttractionImageModelView(applicationContext)
                    ImagesGridLayout(attractionImageModelView)
                }
            }
        }
    }
}

@Composable
fun ImagesGridLayout(viewModel: AttractionImageModelView) {
    val attractionImage: LazyPagingItems<AttractionImage> =
        viewModel.attractionsImageData.collectAsLazyPagingItems()

    Scaffold(
        topBar =
        { TopAppBar() },
        bottomBar = { BottomAppBarMine() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.padding(5.dp, 10.dp)
            ) {
                items(items = attractionImage) { attractionImage ->
                    if (attractionImage != null) {
                        ImagesGridCard(attractionImage = attractionImage)
                    }
                }
            }
        }
    }
}


@Composable
fun ImagesGridCard(attractionImage: AttractionImage) {


//    Image(
//    painter = pai(id = concert.images.get(0).URL),
//    contentDescription = stringResource(id = R.string.dog_content_description))

    Column {

        AsyncImage(
            model = attractionImage.images[0].url,
            contentDescription = "Translated description of what the image contains",
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp, 5.dp)
        )

    }
}

@Composable
fun <T : Any> LazyListScope.itemsPaging(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable (value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                PagingPlaceholderKey(index)
            } else {
                key(item)
            }
        }
    ) { index ->
        val item = items.peek(index)
        itemContent(item)
    }
}}