package com.example.qoolquotes.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qoolquotes.ui.screens.HomeScreen
import com.example.qoolquotes.ui.screens.BrowseScreen
import kotlinx.serialization.Serializable
import androidx.navigation.NavHostController
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.toRoute


@Serializable
object HomeScreenDestination

@Serializable
data class BrowseScreenDestination(
    val selectedView: String,
)

//TODO: Add other screen destinations as examples above

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()


    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController, startDestination = HomeScreenDestination
        ) {
            composable<HomeScreenDestination> {
                HomeScreen()
            }
            composable<BrowseScreenDestination> {
            val args = it.toRoute<BrowseScreenDestination>()
                BrowseScreen(selectedView = args.selectedView)

            }
            //TODO: Add other screen components as examples above
        }
    }
}