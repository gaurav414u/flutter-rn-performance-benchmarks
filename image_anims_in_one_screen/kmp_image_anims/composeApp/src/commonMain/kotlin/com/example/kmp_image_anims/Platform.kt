package com.example.kmp_image_anims

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


@Composable
expect fun getScreenWidth(): Dp

expect fun reportFullyDrawn()