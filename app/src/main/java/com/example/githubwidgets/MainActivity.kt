package com.example.githubwidgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.githubwidgets.ui.theme.GitHubWidgetsTheme
import com.example.githubwidgets.widgets.BasicStatsWidgetReceiver
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubWidgetsTheme {
                val context = this
                NavHost(navController = rememberNavController(), startDestination = "Login") {
                    composable("Login") {
                        LoginScreen(context)
                    }
                    composable("Home", deepLinks = listOf(navDeepLink {
                        uriPattern = "ghwidgets://github?code={code}"
                        action = Intent.ACTION_VIEW
                    }), arguments = listOf(navArgument("code") {
                        type = NavType.StringType
                        defaultValue = ""
                    })
                    ) { entry ->
                        val code = entry.arguments?.getString("code")

                        HomeActivity(context, code)
                    }
                }
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

data class AuthResponse(
    val accessToken: String, val scope: String, val tokenType: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(context: Context, code: String?) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
//    Requesting the access token
//    val client = HttpClient {
//        install(ContentNegotiation)
//    }

//    val clientId = "Iv23li0YWBHEwokiBQQv"
//    val callbackURL = "ghwidgets://github"
//
//    LaunchedEffect(Unit) {
//        scope.launch {
//            val response: HttpResponse =
//                client.post("https://github.com/login/oauth/access_token") {
//                    url {
//                        parameters.append("client_id", clientId)
//                        parameters.append("client_secret", clientSecret)
//                        parameters.append("code", code ?: "")
//                        parameters.append("redirect_uri", callbackURL)
//                    }
//                }
//            if (response.status.value in 200..299) {
//                val data: AuthResponse = response.body()
//                val accessToken = data.accessToken
//                val requestURL = "https://api.github.com/user"
//
//                val userdataResponse = client.get(requestURL) {
//                    headers {
//                        append(HttpHeaders.Accept, "application/vnd.github+json")
//                        append(HttpHeaders.Authorization, "Bearer $accessToken")
//                        append("X-GitHub-Api-Version", "2022-11-28")
//                    }
//                }
//
//                val user: JsonObject = userdataResponse.body()
//                val username = user["login"]?.jsonPrimitive?.content
//                Toast.makeText(context, "Hello $username", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("GH Widgets v0.1.2", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(icon = {
                    Icon(
                        painter = painterResource(R.drawable.code_24px),
                        contentDescription = null
                    )
                },
                    label = { Text(text = "View Source Code") },
                    selected = false,
                    onClick = { /*TODO*/ })
                NavigationDrawerItem(icon = {
                    Icon(
                        painter = painterResource(R.drawable.person_24px),
                        contentDescription = null
                    )
                }, label = { Text(text = "About me") }, selected = false, onClick = { /*TODO*/ })
            }
        },
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Greeting(code ?: "Error") }, navigationIcon = {
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


            Column(Modifier.padding(contentPadding)) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ), modifier = Modifier
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
                            Button({ addWidget(context) }) {
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

@Composable
fun LoginScreen(context: Context) {
    Scaffold { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "GH Widgets",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.size(48.dp))
            Image(
                painter = painterResource(R.drawable.basic_stats_widget),
                contentDescription = "Basic Stats Widget",
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            )
            Spacer(Modifier.size(80.dp))
            Text(
                "Beautiful GitHub Widgets for your Home Screen",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.size(60.dp))
            Button(modifier = Modifier.scale(1.2f), onClick = {
                val clientId = "Iv23li0YWBHEwokiBQQv"
                val callbackURL = "ghwidgets://github"

                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/login/oauth/authorize?client_id=$clientId&redirect_uri=$callbackURL")
                )
                context.startActivity(browserIntent)
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.github_mark_white),
                        contentDescription = "GitHub Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Login with GitHub", fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.align(Alignment.BottomCenter)) {
                Image(
                    painterResource(R.drawable.falling_leaves),
                    contentDescription = "Plant",
                    modifier = Modifier.size(48.dp),

                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.size(52.dp))
            }
        }
    }
}