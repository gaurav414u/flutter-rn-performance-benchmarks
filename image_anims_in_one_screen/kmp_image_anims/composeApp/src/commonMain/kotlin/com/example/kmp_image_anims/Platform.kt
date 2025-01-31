package com.example.kmp_image_anims

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


@Composable
expect fun getScreenWidth(): Int