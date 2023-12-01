package com.example.reviewercompose.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

fun Context.toast(msg: Any) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}

@Composable
fun toast(msg: Any) {
    LocalContext.current.toast("msg")
}