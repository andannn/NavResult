package me.andannn.navresult.sample.navigation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import io.github.andannn.LaunchNavResultHandler
import io.github.andannn.LocalNavResultOwner
import io.github.andannn.rememberNavResultOwner
import io.github.andannn.setNavResult

enum class Screen(val title: String) {
    Home(title = "Home"),
    ScreenA(title = "ScreenA"),
    ScreenB(title = "ScreenB"),
    DialogA(title = "DialogA"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Home.name
    )

    CompositionLocalProvider(
        LocalNavResultOwner provides rememberNavResultOwner()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentScreen.title) },
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = Screen.Home.name) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        var screenANavResult by remember {
                            mutableStateOf<String?>(null)
                        }

                        LaunchNavResultHandler(
                            requestKey = ScreenABackResult,
                            resultSerializer = ScreenAResult.serializer(),
                        ) { result ->
                            screenANavResult = result.toString()
                        }

                        var screenBNavResult by remember {
                            mutableStateOf<String?>(null)
                        }

                        LaunchNavResultHandler(
                            requestKey = ScreenBBackResult,
                            resultSerializer = Int.serializer(),
                        ) { result ->
                            screenBNavResult = result.toString()
                        }

                        var dialogCNavResult by remember {
                            mutableStateOf<String?>(null)
                        }

                        LaunchNavResultHandler(
                            requestKey = DialogCBackResult,
                            resultSerializer = String.serializer(),
                        ) { result ->
                            dialogCNavResult = result
                        }

                        Column {
                            Text("Screen Result From Screen A: $screenANavResult")

                            TextButton(onClick = {
                                navController.navigate(Screen.ScreenA.name)
                            }) {
                                Text(text = "Go To Screen A")
                            }

                            Text("Screen Result From Screen B: $screenBNavResult")

                            TextButton(onClick = {
                                navController.navigate(Screen.ScreenB.name)
                            }) {
                                Text(text = "Go To Screen B")
                            }

                            Text("Screen Result From Dialog C: $dialogCNavResult")

                            TextButton(onClick = {
                                navController.navigate(Screen.DialogA.name)
                            }) {
                                Text(text = "Go To Dialog C")
                            }
                        }
                    }
                }

                composable(route = Screen.ScreenA.name) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val resultOwner = LocalNavResultOwner.current

                        TextButton(onClick = {
                            resultOwner.setNavResult(
                                requestKey = ScreenABackResult,
                                result = ScreenAResult(1, "foo"),
                                serializer = ScreenAResult.serializer()
                            )
                            navController.popBackStack()
                        }) {
                            Text(text = "Send result and Back To Home")
                        }
                    }
                }

                composable(route = Screen.ScreenB.name) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val resultOwner = LocalNavResultOwner.current
                        TextButton(onClick = {
                            resultOwner.setNavResult(
                                ScreenBBackResult,
                                123,
                                Int.serializer()
                            )
                            navController.popBackStack()
                        }) {
                            Text(text = "Back To Home And set Int result")
                        }
                    }
                }

                dialog(
                    route = Screen.DialogA.name,
                    dialogProperties = androidx.compose.ui.window.DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                        usePlatformDefaultWidth = true
                    )
                ) {
                    val resultOwner = LocalNavResultOwner.current
                    AlertDialog(
                        onDismissRequest = {
                            navController.popBackStack()
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                resultOwner.setNavResult(
                                    DialogCBackResult,
                                    "foo",
                                    String.serializer()
                                )
                                navController.popBackStack()
                            }) {
                                Text("Send result and Back To Home")
                            }
                        },
                    )
                }
            }
        }
    }
}

const val ScreenABackResult = "ScreenABackResult"
const val ScreenBBackResult = "ScreenBBackResult"
const val DialogCBackResult = "DialogCBackResult"

@Serializable
data class ScreenAResult(
    val foo: Int,
    val bar: String
)