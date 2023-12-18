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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.presentation.Screen
import com.example.reviewercompose.presentation.screens.auth.ui.AuthScreen
import com.example.reviewercompose.presentation.screens.home.ui.UserPageScreen
import com.example.reviewercompose.presentation.screens.register.ui.RegistrationScreen
import com.example.reviewercompose.presentation.screens.review.creator.ui.ReviewCreationScreen
import com.example.reviewercompose.presentation.screens.review.view.ui.ReviewScreen
import com.example.reviewercompose.presentation.screens.reviews.ui.ReviewListScreen
import com.example.reviewercompose.presentation.theme.ReviewerComposeTheme

class MainActivity : ComponentActivity() {
    private val activityViewModel: MainViewModel by viewModels { MainViewModel.Factory }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReviewerComposeTheme {
                val navController: NavHostController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val userAuthState: UserAuthState by activityViewModel.userAuthState.collectAsStateWithLifecycle()
                Scaffold(
                    bottomBar = {
                        ReviewerBottomAppBar(
                            onClick = { screen -> navController.navigateWithOptionsTo(screen.route) },
                            isCurrent = { screen ->
                                currentBackStack?.destination?.route?.let { route ->
                                    val counted = route.count { it == '/' }
                                    if (counted > 0)
                                        route.split("/")[0]
                                    else route
                                } == screen.route
                            },
                            screenList = userAuthState.availableScreen
                        )
                    }
                ) {
                    ReviewerApp(
                        onSignUpComplete = {
                            navController.navigateWithOptionsTo(Screen.ReviewListScreen.route)
                        },
                        onReviewCreated = {
                            navController.navigateWithStateLoss(Screen.ReviewListScreen.route)
                        },
                        onExit = {
                            navController.navigateWithOptionsTo(Screen.AuthGraph.AuthScreen.route)
                        },
                        onLogin = {
                            navController.navigateWithOptionsTo(Screen.ReviewListScreen.route)
                        },
                        onReviewClick = { reviewID: String ->
                            navController.navigateWithOptionsTo("review/$reviewID")
                        },
                        currentUser = (userAuthState as? UserAuthState.Authorized)?.user,
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
                    icon = { Icon(imageVector = screen.navIcon!!, contentDescription = null) },
                    label = { Text(stringResource(id = screen.label!!)) }
                )
            }
        }
    }
}

@Composable
fun ReviewerApp(
    onSignUpComplete: () -> Unit,
    onReviewCreated: () -> Unit,
    onExit: () -> Unit,
    onLogin: () -> Unit,
    onReviewClick: (String) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    currentUser: User? = null
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ReviewListScreen.route,
        modifier = modifier
    ) {
        composable(Screen.ReviewCreationScreen.route) { _ ->
            ReviewCreationScreen(onReviewCreated, currentUser!!)
        }

        composable(
            route = Screen.ReviewListScreen.route,
            arguments = Screen.ReviewListScreen.args
        ) { _ ->
            ReviewListScreen(onReviewClick)
        }

        composable(
            route = Screen.Review.route,
            arguments = Screen.Review.args
        ) {
            val reviewID = it.arguments!!.getString("id")!!
            ReviewScreen(reviewId = reviewID)
        }

        composable(
            route = Screen.UserProfileScreen.route,
            arguments = Screen.UserProfileScreen.args
        ) {
            UserPageScreen(currentUser!!.id, onExit)
        }

        navigation(
            startDestination = Screen.AuthGraph.AuthScreen.route,
            route = Screen.AuthGraph.route
        ) {
            composable(
                route = Screen.AuthGraph.AuthScreen.route,
                arguments = Screen.AuthGraph.args
            ) {
                AuthScreen(
                    onLogin = onLogin,
                    onGoToRegisterClick = {
                        navController.navigateWithOptionsTo(Screen.AuthGraph.RegisterScreen.route)
                    },
                )
            }

            composable(
                route = Screen.AuthGraph.RegisterScreen.route,
                arguments = Screen.AuthGraph.args
            ) {
                RegistrationScreen(onSignUpComplete)
            }
        }
    }
}

fun NavController.navigateWithOptionsTo(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(Screen.ReviewListScreen.route) {
            this.saveState = true
        }
    }
}

fun NavController.navigateWithStateLoss(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = false
        popUpTo(Screen.ReviewListScreen.route) {
            saveState = false
        }
    }
}