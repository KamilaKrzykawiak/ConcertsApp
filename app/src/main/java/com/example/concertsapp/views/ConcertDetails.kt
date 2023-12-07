package com.example.concertsapp.views

import androidx.activity.ComponentActivity
import android.content.Context
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concertsapp.models.EventDetails
import com.example.concertsapp.modelviews.EventDetailsModelView
import com.example.concertsapp.ui.theme.ConcertsAppTheme

class ConcertDetails : ComponentActivity() {

    lateinit var eventDetailsModelView: EventDetailsModelView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        var id: String
        setContent {
            ConcertsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    if (extras != null) {
                        id = extras.getString("id").toString();
                        // and get whatever type user account id is

                        eventDetailsModelView = EventDetailsModelView(applicationContext)

                        eventDetailsModelView.eventDetailsDataAccess.getEventDetails(id)
                        ConcertDetailsLayout(eventDetailsModelView, this)
                    }
                }
            }
        }
    }
}


@Composable
fun ConcertDetailsLayout(viewModel: EventDetailsModelView, activityContext: Context) {
    val eventDetails: EventDetails? =
        viewModel.eventDetailsDataAccess.readEventDetail.observeAsState().value
    Scaffold(
        topBar =
        { TopAppBar() },
        bottomBar = { BottomAppBarMine(activityContext) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp, 5.dp)
                    .align(Center)
                    .fillMaxWidth()
            ) {
                if (eventDetails != null) {

                    Text(
                        "Event name: " + (eventDetails.name), color = Color.Black,
                        modifier = Modifier
                            .padding(5.dp, 5.dp)
                            .align(alignment = CenterHorizontally)
                    )
                    AsyncImage(
                        model = (eventDetails.images.get(0).url),
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier
                            .size(130.dp)
                            .padding(5.dp, 5.dp)
                            .border(2.dp, Color.DarkGray, RoundedCornerShape(15))
                            .align(alignment = CenterHorizontally)

                    )
                    Text(
                        "Description: " + (eventDetails.description ?: "No data available"),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(5.dp, 5.dp)
                            .align(alignment = CenterHorizontally)
                    )
                    Text(
                        "Info: " + (eventDetails.info ?: "No data available"), color = Color.Black,
                        modifier = Modifier
                            .padding(5.dp, 5.dp)
                            .align(alignment = CenterHorizontally)

                    )
                    Text(
                        "Additional info: " + (eventDetails.additionalInfo ?: "No data available"),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(5.dp, 5.dp)
                            .align(alignment = CenterHorizontally)
                    )

                }

            }
        }
    }
}


//
//
////activity
////poukładać widok
////dodać do manifestu - done
////dodać pobieranie danych - z ID pobieram id eventu,
//
////    w onCreate =>
////
////    Bundle extras = getIntent().getExtras();
////    String eventId;
////
////    if (extras != null) {
////        eventId= extras.getString("eventId");
////        // and get whatever type user account id is
////    }
////w url podawać event ID zamiast search
////eventId musi być przekazany tak samo jak search w EventsDataAccess
////URL https://app.ticketmaster.com/discovery/v2/events/${eventId }.json?apikey=qDPB4SA8ARvlbWPdL90LHd3oV2GsTQka&size=21&page=${page}