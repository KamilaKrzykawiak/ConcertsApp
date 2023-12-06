package com.example.concertsapp.views


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.concertsapp.models.Event
import com.example.concertsapp.modelviews.EventModelView
import com.example.concertsapp.ui.theme.ConcertsAppTheme


class ConcertList : ComponentActivity() {
    lateinit var eventModelView: EventModelView

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
data class PagingPlaceholderKey(private val index: Int) : Parcelable {

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
fun ConcertCard(event: Event) {


//    Image(
//    painter = pai(id = concert.images.get(0).URL),
//    contentDescription = stringResource(id = R.string.dog_content_description))

    Column {

        AsyncImage(
            model = event.images[0].url,
            contentDescription = "Translated description of what the image contains",
            modifier = Modifier
                .size(130.dp)
                .padding(5.dp, 5.dp)
                .border(2.dp, androidx.compose.ui.graphics.Color.DarkGray, RoundedCornerShape(20))
        )
        Text(
            text = event.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
                .padding(5.dp, 5.dp)
        )

        Text(
            text = "Type: " + event.type,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
                .padding(5.dp, 5.dp)
        )
    }
}

@Composable
fun ConcertCardLayout(viewModel: EventModelView) {
    val events: LazyPagingItems<Event> = viewModel.eventsData.collectAsLazyPagingItems()

    Scaffold(
        topBar =
        { TopAppBar() },
        bottomBar = { BottomAppBarMine() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 125.dp),
                modifier = Modifier.padding(5.dp, 10.dp),

                ) {
                // Add a single item
                itemsPaging(items = events) { events ->
                    if (events != null) {
                        ConcertCard(event = events)
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
//zmienić na BottomNavigation https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#BottomNavigation(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,kotlin.Function1)
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
                    IconButton(onClick = {
                        /* lista wydarzeń */
//                        val appContext =  LocalContext.current
//                        Intent(appContext,ImagesGrid::class.java).also {
//                            startActivity(it)}
                        })
                     {
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



