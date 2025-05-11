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
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.ui.screens.EditScreen
import com.example.qoolquotes.ui.screens.QuoteScreen
import com.example.qoolquotes.ui.screens.SearchScreen
import com.example.qoolquotes.ui.screens.SettingsScreen
import com.example.qoolquotes.ui.screens.SlideshowScreen


@Serializable
object HomeScreenDestination

@Serializable
object SearchScreenDestination

@Serializable
object SettingsScreenDestination

@Serializable
object QuoteScreenDestination

@Serializable
object EditScreenDestination

@Serializable
object SlideshowScreenDestination

@Serializable
data class BrowseScreenDestination(
    val selectedView: String,
)


val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun NavGraph(modifier: Modifier = Modifier, quoteDao: QuoteDao) {
    val navController = rememberNavController()


    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController, startDestination = HomeScreenDestination
        ) {
            composable<HomeScreenDestination> { HomeScreen() }
            composable<SearchScreenDestination> { SearchScreen() }
            composable<SettingsScreenDestination> { SettingsScreen() }
            composable<QuoteScreenDestination> { QuoteScreen(quoteDao) }
            composable<EditScreenDestination> { EditScreen() }
            composable<SlideshowScreenDestination> { SlideshowScreen() }
            composable<BrowseScreenDestination> {
                val args = it.toRoute<BrowseScreenDestination>()
                BrowseScreen(selectedView = args.selectedView)
            }
        }
    }
}