package com.example.reviewercompose.presentation.screens.auth.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
class AuthInputStateHolder(
    val hint: AuthHint,
    initialLogin: String,
    initialPassword: String,
) {
    var login by mutableStateOf(initialLogin)
        private set

    var password by mutableStateOf(initialPassword)
        private set

    fun updateLoginInput(login: String) {
        this.login = login
    }

    fun updatePasswordInput(login: String) {
        this.password = login
    }

    data class AuthHint(
        val loginHint: String,
        val passwordHint: String,
    )

    companion object {
        val saver: Saver<AuthInputStateHolder, *> = listSaver(
            save = { authInput ->
                listOf(
                    authInput.hint.loginHint,
                    authInput.hint.passwordHint,
                    authInput.login,
                    authInput.password
                )
            },
            restore = { list ->
                val hint = AuthHint(loginHint = list[0], passwordHint = list[1])
                AuthInputStateHolder(
                    hint = hint,
                    initialLogin = list[2],
                    initialPassword = list[3]
                )
            }
        )
    }
}

@Composable
fun rememberAuthInputState(hint: AuthInputStateHolder.AuthHint): AuthInputStateHolder {
    return remember(hint) {
        AuthInputStateHolder(hint, "", "")
    }
}

@Composable
fun rememberAuthInputSavable(hint: AuthInputStateHolder.AuthHint): AuthInputStateHolder {
    return rememberSaveable(hint, saver = AuthInputStateHolder.saver) {
        AuthInputStateHolder(hint, "", "")
    }
}
