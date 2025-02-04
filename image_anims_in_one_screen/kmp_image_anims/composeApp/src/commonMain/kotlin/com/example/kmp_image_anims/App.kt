package com.example.kmp_image_anims

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kmp_image_anims.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        GridScreen()
    }
}

val LocalImageWidth = staticCompositionLocalOf<Dp> { error("Column width not provided") }
val LocalImageWidthPx = staticCompositionLocalOf<Int> { error("Column width not provided") }


@Composable
fun GridScreen() {
    // Get screen width from LocalConfiguration
    val screenWidth = getScreenWidth()
    val imageWidth = screenWidth / 10

    val imageWidthPx = with(
        androidx.compose.ui.platform.LocalDensity.current
    ) { imageWidth.toPx().toInt() }

    val infiniteTransition = rememberInfiniteTransition()
    CompositionLocalProvider(
        LocalImageWidth provides imageWidth,
        LocalImageWidthPx provides imageWidthPx
    ) {
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GridImage(index: Int, modifier: Modifier = Modifier) {
    val columnWidthPx = LocalImageWidthPx.current
    val columnWidth = LocalImageWidth.current
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(Res.getUri("drawable/${index % 20}.jpeg"))
            .size(
                columnWidthPx,
                columnWidthPx
            ) // Equivalent to cacheHeight and cacheWidth in Flutter
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier.aspectRatio(1f)
            .size(columnWidth, columnWidth)
    )
}
