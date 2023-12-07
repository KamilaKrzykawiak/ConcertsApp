package com.example.concertsapp.views


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    color = MaterialTheme.colors.background
                ) {
                    eventModelView = EventModelView(applicationContext)
                    ConcertCardLayout(eventModelView, this)
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
fun ConcertCard(event: Event, activityContext: Context) {


//    Image(
//    painter = pai(id = concert.images.get(0).URL),
//    contentDescription = stringResource(id = R.string.dog_content_description))

    Column(modifier = Modifier.clickable {
        val myIntent = Intent(activityContext, ConcertDetails::class.java)
        myIntent.putExtra("id", event.id)
        activityContext.startActivity(myIntent)
    }) {


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
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
                .padding(5.dp, 5.dp)
        )

        Text(
            text = "Type: " + event.type,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
                .padding(5.dp, 5.dp)
        )
    }
}

@Composable
fun ConcertCardLayout(viewModel: EventModelView, activityContext: Context) {
    val events: LazyPagingItems<Event> = viewModel.eventsData.collectAsLazyPagingItems()
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar =
        { TopAppBar() },
        bottomBar = { BottomAppBarMine(activityContext) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(5.dp)
        ) {
            Column() {

                Row(
                    modifier = Modifier
                        .background(color = Color.LightGray),

                    ) {

                    IconButton(

                        onClick = {
                            viewModel.setSearch(text)
                            events.refresh()
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description",
                        )
                    }
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.LightGray),

                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Search", color = Color.Black) }
                    )

                }
            }
            Row(
                modifier = Modifier
                    .padding(top = (60.dp))

            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),


                    ) {
                    // Add a single item
                    itemsPaging(items = events) { events ->
                        if (events != null) {
                            ConcertCard(event = events, activityContext)
                        }
                    }
                }
            }
        }
    }
}



