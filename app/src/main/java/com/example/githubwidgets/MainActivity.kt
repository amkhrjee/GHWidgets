package com.example.githubwidgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.githubwidgets.ui.theme.GitHubWidgetsTheme
import com.example.githubwidgets.widgets.BasicStatsWidgetReceiver
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubWidgetsTheme {
                HomeActivity(context = this)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = name,
        modifier = Modifier,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
    )
}


fun addWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val myProvider = ComponentName(context, BasicStatsWidgetReceiver::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {

        appWidgetManager.requestPinAppWidget(myProvider, null, null)
    } else {
        Toast.makeText(context, "Request Pin App Widget not supported", Toast.LENGTH_SHORT).show()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(context: Context) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { Text("hello") }
        },
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Greeting("amkhrjee") }, navigationIcon = {
                IconButton({ scope.launch { drawerState.apply { if (isClosed) open() else close() } } }) {
                    Icon(
                        Icons.Rounded.Person, contentDescription = "Avatar"
                    )
                }
            }, actions = {
                TextButton({
                    showBottomSheet = true
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.FavoriteBorder,
                            "Support",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.padding(2.dp))
                        Text(
                            "Support",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            })
        }) { contentPadding ->
            val buttonHorizontalPadding = 8.dp
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                ) {
                    Text(
                        "Your donations help keep this project going ❤️",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(Modifier.padding(vertical = 8.dp))

                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            OutlinedButton(onClick = {}, Modifier.weight(1f)) {
                                Text("PayPal")
                            }
                            Spacer(Modifier.size(buttonHorizontalPadding))
                            OutlinedButton(onClick = {}, Modifier.weight(1f)) {
                                Text("Ko-Fi")
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            OutlinedButton(onClick = {}, Modifier.weight(1f)) {
                                Text("Gift a Coffee")
                            }
                            Spacer(Modifier.size(buttonHorizontalPadding))
                            OutlinedButton(onClick = {}, Modifier.weight(1f)) {
                                Text("Star on GitHub")
                            }
                        }
                    }

                    Spacer(Modifier.padding(vertical = 8.dp))

                    Text(
                        "Made in India \uD83C\uDDEE\uD83C\uDDF3",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(Modifier.padding(vertical = 8.dp))
                }
            }

//            Test Button

            Column(Modifier.padding(contentPadding)) {
//                Text("Your Widgets", style = MaterialTheme.typography.titleMedium)
//                Spacer(Modifier.size(6.dp))
//                Button({
//                    addWidget(context)
//                }) { Text("Pin Widget") }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Basic Stats Widget",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Button({addWidget(context)}) {
                                Text("Add")
                            }
                        }
                        Spacer(Modifier.size(8.dp))
                        Image(
                            painter = painterResource(R.drawable.basic_stats_widget),
                            contentDescription = "Basic Stats Widget",
                            modifier = Modifier.clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
        }
    }
}