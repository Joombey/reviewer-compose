package com.example.reviewercompose

object ServiceLocator {
    val isInited = false
    fun init() {
        if (isInited) return
    }
}