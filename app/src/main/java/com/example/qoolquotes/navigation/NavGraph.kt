package com.example.qoolquotes.navigation


import QuoteScreen
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
import com.example.qoolquotes.ui.screens.AddQuoteScreen
import com.example.qoolquotes.ui.screens.EditScreen
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
object EditScreenDestination

@Serializable
object SlideshowScreenDestination

@Serializable
object AddQuoteScreenDestination

@Serializable
data class BrowseScreenDestination(
    val selectedView: String,
)

@Serializable
data class QuoteScreenDestination(
    val quoteId: Long? = null
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
            composable<HomeScreenDestination> { HomeScreen(quoteDao = quoteDao) }
            composable<SearchScreenDestination> { SearchScreen() }
            composable<AddQuoteScreenDestination> { AddQuoteScreen(quoteDao = quoteDao) }
            composable<SettingsScreenDestination> { SettingsScreen() }
            composable<EditScreenDestination> { EditScreen() }
            composable<SlideshowScreenDestination> { SlideshowScreen() }
            composable<BrowseScreenDestination> {
                val args = it.toRoute<BrowseScreenDestination>()
                BrowseScreen(selectedView = args.selectedView, quoteDao = quoteDao)
            }
            composable<QuoteScreenDestination> { backStackEntry ->
                val args = backStackEntry.toRoute<QuoteScreenDestination>()
                QuoteScreen(
                    quoteId = args.quoteId,
                    quoteDao = quoteDao
                )
            }
        }
    }
}