package com.example.reviewercompose.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.SensorDoor
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.reviewercompose.R

sealed interface Screen {
    val route: String
    val navIcon: ImageVector? get() = null

    @get:StringRes
    val label: Int? get() = null
    val args: List<NamedNavArgument>
        get() = emptyList()

    object ReviewListScreen : Screen {
        override val route: String = "reviews"
        override val navIcon: ImageVector = Icons.Filled.ViewList
        override val label: Int = R.string.reviews_screen_label
    }

    object Review : Screen {
        override val route: String = "review/{id}"
        override val label: Int = R.string.reviews_screen_label
        override val args: List<NamedNavArgument> = listOf(
            navArgument("id") { type = NavType.StringType }
        )
    }


    object ReviewCreationScreen : Screen {
        override val route: String = "create-review"
        override val navIcon: ImageVector = Icons.Filled.AddCircle
        override val label: Int = R.string.review_creation_screen_label
    }

    object UserProfileScreen : Screen {
        override val route: String = "user?id={id}"
        override val navIcon: ImageVector = Icons.Filled.AccountCircle
        override val label: Int = R.string.profile_screen_label
        override val args = listOf(
            navArgument("id") {
                nullable = true
                type = NavType.StringType
            }
        )

        inline fun buildRouteWithArguments(block: (String, NavArgument) -> Unit): String {
            var resultRoute = "user?"
            for (arg in args) {
                resultRoute += "${arg.name}=${block(arg.name, arg.argument)}&"
            }
            return resultRoute.slice(0 until (resultRoute.length - 1))
        }
    }

    object AuthGraph : Screen {
        override val route: String = "auth"
        override val navIcon: ImageVector = Icons.Filled.SensorDoor
        override val label: Int = R.string.sign_in_screen_label

        object AuthScreen : Screen {
            override val route: String = "${AuthGraph.route}/sign-in"
        }

        object RegisterScreen : Screen {
            override val route: String = "${AuthGraph.route}/sign-up"
        }
    }

    companion object {
        val authenticatedScreens = listOf(
            ReviewListScreen,
            ReviewCreationScreen,
            UserProfileScreen
        )

        val unAuthenticatedScreens = listOf(
            ReviewListScreen,
//            ReviewCreationScreen,
            AuthGraph
        )
    }
}