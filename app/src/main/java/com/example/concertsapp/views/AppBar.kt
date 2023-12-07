package com.example.concertsapp.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
        scrollBehavior = scrollBehavior,
    )
}

@Composable

fun BottomAppBarMine(activityContext: Context) {
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
                        if (activityContext.javaClass != ConcertList::class.java) {
                            val myIntent = Intent(activityContext, ConcertList::class.java)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            activityContext.startActivity(myIntent)
                        }
                    })
                    {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Localized description"
                        )
                    }
                    Text("Events",  color = Color.Black)
                }

                Column(
//

                ) {
                    IconButton(onClick = {

                        /* zdjęcia */
                        if (activityContext.javaClass != ImagesGrid::class.java) {
                            val myIntent = Intent(activityContext, ImagesGrid::class.java)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            activityContext.startActivity(myIntent)
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountBox,
                            contentDescription = "Localized description"
                        )
                    }
                    Text("Gallery", color = Color.Black)
                }
            }
        }
    )
}
