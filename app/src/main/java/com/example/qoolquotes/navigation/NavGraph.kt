package com.example.qoolquotes.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qoolquotes.ui.screens.HomeScreen
import com.example.qoolquotes.ui.screens.BrowseScreen
import kotlinx.serialization.Serializable


@Serializable
object HomeScreenDestination

@Serializable
data class BrowseScreenDestination(
    val selectedView: String?,
)

//TODO: Add other screen destinations as examples above

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = HomeScreenDestination
    ) {
        composable<HomeScreenDestination> {
            HomeScreen(navController)
        }
        composable<BrowseScreenDestination> {
            //TODO: Maybe use optional parameter maybe don't
//            val args = it.toRoute<BrowseScreenDestination>()
            BrowseScreen(navController)

        }
        //TODO: Add other screen components as examples above
    }
}