package com.example.qoolquotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.ui.screens.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenDestination

@Serializable
object SearchScreenDestination

@Serializable
object SettingsScreenDestination

@Serializable
object TutorialScreenDestination

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
    val quoteId: Int? = null
)

@Serializable
data class EditScreenDestination(
    val quoteId: Int? = null
)

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    quoteDao: QuoteDao,
    onChangeTheme: () -> Unit,
    selectedFont: String,
    onFontChange: (String) -> Unit
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController, startDestination = HomeScreenDestination
        ) {
            composable<HomeScreenDestination> { HomeScreen() }
            composable<SearchScreenDestination> { SearchScreen() }
            composable<AddQuoteScreenDestination> { AddQuoteScreen(quoteDao = quoteDao) }
            composable<SettingsScreenDestination> {
                SettingsScreen(
                    onChangeTheme = onChangeTheme,
                    onDeleteAllQuotes = {
                        scope.launch { quoteDao.deleteAllQuotes() }
                    },
                    selectedFont = selectedFont,
                    onFontChange = onFontChange
                )
            }
            composable<TutorialScreenDestination> { TutorialScreen() }
            composable<SlideshowScreenDestination> { SlideshowScreen() }
            composable<BrowseScreenDestination> {
                val args = it.toRoute<BrowseScreenDestination>()
                BrowseScreen(selectedView = args.selectedView, quoteDao = quoteDao)
            }
            composable<QuoteScreenDestination> { backStackEntry ->
                val args = backStackEntry.toRoute<QuoteScreenDestination>()
                QuoteScreen(
                    quoteId = args.quoteId, quoteDao = quoteDao
                )
            }
            composable<EditScreenDestination> { backStackEntry ->
                val args = backStackEntry.toRoute<EditScreenDestination>()
                EditScreen(quoteId = args.quoteId, quoteDao = quoteDao)
            }
        }
    }
}
