package com.example.reviewercompose.presentation.screens.register.ui

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.reviewercompose.presentation.screens.register.RegistrationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.R
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.presentation.screens.auth.ui.AuthInputStateHolder
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerButton
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerText
import com.example.reviewercompose.presentation.screens.auth.ui.rememberAuthInputSavable
import com.example.reviewercompose.utils.toast
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    onSignUpComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = viewModel(factory = RegistrationViewModel.Factory)
) {
    val scope = rememberCoroutineScope()
    val latestSignUpAction by rememberUpdatedState(onSignUpComplete)
    var chosenIconUri: Uri? by rememberSaveable { mutableStateOf(null) }
    val context = LocalContext.current
    val registry = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri == null) return@rememberLauncherForActivityResult
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            chosenIconUri = uri
        }
    )
    Surface(modifier.padding(8.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserIcon(
                uri = chosenIconUri,
                onSetUserIcon = { registry.launch(arrayOf("image/*")) },
                modifier = modifier.weight(1f)
            )
            val state: AuthInputStateHolder = rememberAuthInputSavable(
                AuthInputStateHolder.AuthHint(
                    stringResource(R.string.login_screen_label),
                    stringResource(R.string.password_screen_label)
                )
            )
            RegistrationBody(
                onLoginClick = { login, password ->
                    scope.launch {
                        val user = User(login, login, chosenIconUri.toString())
                        val registerJob = viewModel.register(user, login, password)
                        registerJob.join()
                        if (!registerJob.isCancelled) latestSignUpAction()
                        else context.toast("Упс")
                    }
                },
                authInputStateHolder = state,
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun RegistrationBody(
    onLoginClick: (login: String, password: String) -> Unit,
    authInputStateHolder: AuthInputStateHolder,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ReviewerText(
            hint = stringResource(R.string.login_screen_label),
            text = authInputStateHolder.login,
            onTextChange = authInputStateHolder::updateLoginInput,
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth()
        )

        ReviewerText(
            hint = authInputStateHolder.hint.passwordHint,
            text = authInputStateHolder.password,
            onTextChange = authInputStateHolder::updatePasswordInput,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        ReviewerButton(
            text = stringResource(R.string.sign_up_label),
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
}

@Composable
fun UserIcon(
    uri: Uri?,
    onSetUserIcon: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        if (uri == null) {
            Image(
                contentDescription = null,
                contentScale = ContentScale.Crop,
                imageVector = Icons.Filled.AddBox,
                modifier = Modifier
                    .size(100.dp)
                    .clickable(onClick = onSetUserIcon)
            )
        } else {
            val contentResolver = LocalContext.current.contentResolver
            val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            Image(
                contentDescription = null,
                contentScale = ContentScale.Crop,
                bitmap = bitmap.asImageBitmap(),
                modifier = Modifier
                    .size(100.dp)
                    .clickable(onClick = onSetUserIcon)
            )
        }
    }
}