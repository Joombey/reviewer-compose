package com.example.reviewercompose.presentation.screens.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.R
import com.example.reviewercompose.presentation.screens.auth.AuthViewModel
import com.example.reviewercompose.presentation.theme.PlaceholderGreen
import com.example.reviewercompose.presentation.theme.PlaceholderPink

@Composable
fun AuthScreen(
    onLoginClick: (login: String, password: String) -> Unit,
    onGoToRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    userPageViewModel: AuthViewModel = viewModel()
) {
    val authInputStateHolder = rememberAuthInputSavable(
        hint = AuthInputStateHolder.AuthHint(
            loginHint = stringResource(R.string.login_screen_label),
            passwordHint = stringResource(R.string.password_screen_label),
        )
    )
    Surface(modifier.padding(8.dp)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 20.dp)
            ) {
                ReviewerTextField(
                    hint = authInputStateHolder.hint.loginHint,
                    text = authInputStateHolder.login,
                    onTextChange = authInputStateHolder::updateLoginInput,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth()
                )

                ReviewerTextField(
                    hint = authInputStateHolder.hint.passwordHint,
                    text = authInputStateHolder.password,
                    onTextChange = authInputStateHolder::updatePasswordInput,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                ReviewerButton(
                    text = stringResource(R.string.sign_in_screen_label),
                    onClick = {
                        onLoginClick(
                            authInputStateHolder.login,
                            authInputStateHolder.password
                        )
                    },
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                        )
                    )
                )
            }

            Text(
                text = "Перейти к регистрации",
                style = MaterialTheme.typography.labelMedium,
                color = PlaceholderGreen,
                modifier = Modifier
                    .clickable { onGoToRegisterClick() }
            )
        }
    }
}

@Composable
fun ReviewerButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Center,
        text = text,
        color = Color.White,
        modifier = modifier
            .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
            .padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewerTextField(
    hint: String,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle? = null,
    singleLine: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Surface {
        TextField(
            value = text,
            onValueChange = onTextChange,
            visualTransformation = visualTransformation,
            placeholder = {
                Text(
                    text = hint,
                    style = textStyle ?: MaterialTheme.typography.bodySmall,
                    color = PlaceholderPink,
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = keyboardType
            ),
            trailingIcon = trailingIcon,
            modifier = modifier,
            textStyle = textStyle ?: MaterialTheme.typography.bodySmall,
            singleLine = singleLine
        )
    }
}