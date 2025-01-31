package com.example.android_image_anims

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GridScreen()

        }
    }
}

val LocalImageWidth = staticCompositionLocalOf<Int> { error("Column width not provided") }


@Composable
fun GridScreen() {
    // Get screen width from LocalConfiguration
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val imageWidth = screenWidth / 10

    val infiniteTransition = rememberInfiniteTransition()
    CompositionLocalProvider(LocalImageWidth provides imageWidth) {
        LazyVerticalGrid(columns = GridCells.Fixed(10)) {
            items(200) { index ->
                when (index % 3) {
                    0 -> GridRotateItem(index, infiniteTransition)
                    1 -> GridFadeItem(index, infiniteTransition)
                    else -> GridScaleItem(index, infiniteTransition)
                }
            }
        }
    }
}

@Composable
fun GridRotateItem(index: Int, infiniteTransition: InfiniteTransition) {
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    GridImage(index, Modifier.graphicsLayer(rotationZ = rotation))
}

@Composable
fun GridFadeItem(index: Int, infiniteTransition: InfiniteTransition) {
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    GridImage(index, Modifier.alpha(alpha))
}

@Composable
fun GridScaleItem(index: Int, infiniteTransition: InfiniteTransition) {
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    GridImage(index, Modifier.scale(scale))
}

@Composable
fun GridImage(index: Int, modifier: Modifier = Modifier) {
    val columnWidth = LocalImageWidth.current
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("file:///android_asset/images/${index % 20}.jpeg")
            .size(columnWidth, columnWidth) // Equivalent to cacheHeight and cacheWidth in Flutter
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier.aspectRatio(1f)
    )
}
