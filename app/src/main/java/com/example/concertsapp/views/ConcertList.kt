package com.example.concertsapp.views


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import coil.compose.AsyncImage
import com.example.concertsapp.models.Concert
import com.example.concertsapp.models.Image
import com.example.concertsapp.modelviews.EventModelView
import com.example.concertsapp.ui.theme.ConcertsAppTheme


class ConcertList : ComponentActivity() {
    val eventModelView = EventModelView()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConcertsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ConcertCardLayout(eventModelView)


                }
            }
        }
        eventModelView.getEvents(applicationContext)
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
                .border(2.dp, androidx.compose.ui.graphics.Color.Black, RectangleShape)
        )
        Text(
            text = concert.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(130.dp)
        )

        Text(
            text = "Type: " + concert.type,
            textAlign = TextAlign.Center,
//            fontSize = 3.dp,
            modifier = Modifier
                .width(130.dp),
        )
    }
}

@Composable
fun ConcertCardLayout(viewModel:EventModelView) {
    val events = viewModel.readEvents.observeAsState().value
    Scaffold(
        topBar =
        { TopAppBar() },
        bottomBar = { BottomAppBarMine() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 125.dp),
                modifier = Modifier.border(
                    2.dp,
                    androidx.compose.ui.graphics.Color.Black,
                    RectangleShape
                )

            ) {
                // Add a single item
                items(events.orEmpty()) { concert ->
                    ConcertCard(concert = concert)
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
