package com.example.reviewercompose.utils

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: Any) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}