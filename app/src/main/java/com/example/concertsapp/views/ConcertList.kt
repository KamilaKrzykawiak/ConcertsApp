package com.example.concertsapp.views


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.lightColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.concertsapp.models.Concert
import com.example.concertsapp.models.Image
import com.example.concertsapp.modelviews.EventModelView
import com.example.concertsapp.ui.theme.ConcertsAppTheme
import com.example.concertsapp.ui.theme.Purple500


class ConcertList : ComponentActivity() {
    lateinit var eventModelView : EventModelView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConcertsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    eventModelView = EventModelView(applicationContext)
                    ConcertCardLayout(eventModelView)
                }
            }
        }
    }
}

fun <T : Any> LazyGridScope.itemsPaging(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
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
        itemContent(items[index])
    }
}

@SuppressLint("BanParcelableUsage")
private data class PagingPlaceholderKey(private val index: Int) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) = parcel.writeInt(index)

    override fun describeContents(): Int = 0

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<PagingPlaceholderKey> =
            object : Parcelable.Creator<PagingPlaceholderKey> {
                override fun createFromParcel(parcel: Parcel) =
                    PagingPlaceholderKey(parcel.readInt())
                override fun newArray(size: Int) = arrayOfNulls<PagingPlaceholderKey?>(size)
            }
    }
}


@Composable
fun ConcertCard(concert: Concert) {


//    Image(
//    painter = pai(id = concert.images.get(0).URL),
//    contentDescription = stringResource(id = R.string.dog_content_description))

    Column {

        AsyncImage(
            model = concert.images[0].url,
            contentDescription = "Translated description of what the image contains",
            modifier = Modifier
                .size(130.dp)
                .padding(5.dp, 5.dp)
                .border(2.dp, androidx.compose.ui.graphics.Color.DarkGray, RoundedCornerShape(20))
        )
        Text(
            text = concert.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
                .padding(5.dp, 5.dp)
        )

        Text(
            text = "Type: " + concert.type,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
                .padding(5.dp, 5.dp)
        )
    }
}

@Composable
fun ConcertCardLayout(viewModel:EventModelView) {
    val events: LazyPagingItems<Concert> = viewModel.eventsData.collectAsLazyPagingItems()

    Scaffold(
        topBar =
        { TopAppBar() },
        bottomBar = { BottomAppBarMine() }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 125.dp),
                modifier = Modifier.padding(5.dp, 10.dp),

            ) {
                // Add a single item
                itemsPaging(items = events) { concert ->
                    if (concert != null) {
                        ConcertCard(concert = concert)
                    }
                }
            }
        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(Color.Gray),
        title = {
            Text(
                "Concerts App",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(130.dp)
            )
        },
        actions = {
            IconButton(onClick = { /* wyswietlac pole do wyszukiwania w miejscu nazwy apki */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}


@Composable
fun BottomAppBarMine() {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                ) {
                    IconButton(onClick = { /* lista wydarzeń */ }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Localized description"
                        )
                    }
                    Text("Events")
                }

                Column(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                ) {
                    IconButton(onClick = { /* zdjęcia */ }) {
                        Icon(
                            imageVector = Icons.Filled.AccountBox,
                            contentDescription = "Localized description"
                        )
                    }
                    Text("Gallery")
                }
            }
        }
    )
}


