package com.example.reviewercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reviewercompose.presentation.Screen
import com.example.reviewercompose.presentation.screens.auth.ui.AuthScreen
import com.example.reviewercompose.presentation.screens.home.ui.UserPageScreen
import com.example.reviewercompose.presentation.screens.review.creator.ui.ReviewCreationScreen
import com.example.reviewercompose.presentation.screens.reviews.ui.ReviewListScreen
import com.example.reviewercompose.presentation.theme.ReviewerComposeTheme

class MainActivity : ComponentActivity() {
    private val activityViewModel: MainViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReviewerComposeTheme {
                val navController: NavHostController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val availableScreens: List<Screen> by activityViewModel.availableScreens.collectAsStateWithLifecycle()
                Scaffold(
                    bottomBar = {
                        ReviewerBottomAppBar(
                            onClick = { screen -> navController.navigate(screen.route) },
                            isCurrent = { screen -> currentBackStack?.destination?.route == screen.route },
                            screenList = availableScreens
                        )
                    }
                ) {
                    ReviewerApp(
                        navController = navController,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewerBottomAppBar(
    screenList: List<Screen>,
    onClick: (Screen) -> Unit,
    isCurrent: (Screen) -> Boolean,
) {
    BottomAppBar {
        for (screen in screenList) {
            key(screen.route) {
                NavigationBarItem(
                    selected = isCurrent(screen),
                    onClick = { onClick(screen) },
                    icon = { Icon(imageVector = screen.navIcon, contentDescription = null) }
                )
            }
        }
    }
}

@Composable
fun ReviewerApp(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ReviewListScreen.route,
        modifier = modifier
    ) {
        composable(Screen.ReviewCreationScreen.route) { navBackStackEntry ->
            ReviewCreationScreen()
        }

        composable(
            route = Screen.ReviewListScreen.route,
            arguments = Screen.ReviewListScreen.args
        ) { navBackStackEntry ->
            ReviewListScreen()
        }

        composable(
            route = Screen.UserProfileScreen.route,
            arguments = Screen.UserProfileScreen.args
        ) {
            UserPageScreen()
        }

        composable(
            route = Screen.AuthScreen.route,
            arguments = Screen.AuthScreen.args
        ) {
            AuthScreen()
        }
    }
}