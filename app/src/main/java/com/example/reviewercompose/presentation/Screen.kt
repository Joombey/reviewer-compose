package com.example.reviewercompose.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Screen {
    val route: String
    val navIcon: ImageVector
    val args: List<NamedNavArgument>
        get() = emptyList()

    object ReviewListScreen : Screen {
        override val route: String = "reviews"
        override val navIcon: ImageVector = Icons.Filled.ViewList
    }


    object ReviewCreationScreen : Screen {
        override val route: String = "create-review"
        override val navIcon: ImageVector = Icons.Filled.AddCircle
    }

    object UserProfileScreen : Screen {
        override val route: String = "user/{id}"
        override val navIcon: ImageVector = Icons.Filled.AccountCircle
        override val args = listOf(
            navArgument("id") {
                nullable = true
                type = NavType.StringType
            }
        )
    }

    object AuthScreen : Screen by UserProfileScreen {
        override val route: String = "auth"
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    companion object {
        val authenticatedScreens = listOf(
            ReviewListScreen,
            ReviewCreationScreen,
            UserProfileScreen
        )

        val unAuthenticatedScreens = listOf(
            ReviewListScreen,
            ReviewCreationScreen,
            AuthScreen
        )
    }
}