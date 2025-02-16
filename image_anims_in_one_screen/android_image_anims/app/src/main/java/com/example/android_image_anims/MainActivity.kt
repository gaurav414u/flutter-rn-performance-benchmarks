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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
val LocalImageWidthPx = staticCompositionLocalOf<Int> { error("Column width not provided") }

@Composable
fun GridScreen() {
    // Get screen width from LocalConfiguration
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val imageWidth = screenWidth / 10
    val imageWidthPx = with(
        androidx.compose.ui.platform.LocalDensity.current
    ) { imageWidth.dp.toPx().toInt() }

    var imagesLoaded by remember { mutableStateOf(0) }
    val allImagesLoaded by remember { derivedStateOf { imagesLoaded == 200 } }
    val context = LocalContext.current
    val updatedContext = rememberUpdatedState(context)

    val infiniteTransition = rememberInfiniteTransition()
    CompositionLocalProvider(
        LocalImageWidth provides imageWidth,
        LocalImageWidthPx provides imageWidthPx
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(10)) {
            items(200) { index ->
                when (index % 3) {
                    0 -> GridRotateItem(index, infiniteTransition) {
                        imagesLoaded++
                    }

                    1 -> GridFadeItem(index, infiniteTransition) {
                        imagesLoaded++
                    }

                    else -> GridScaleItem(index, infiniteTransition) {
                        imagesLoaded++
                    }
                }
            }
        }
        LaunchedEffect(allImagesLoaded) {
            if (allImagesLoaded) {
                val activity =
                    updatedContext.value as? ComponentActivity // Access with .value and cast
                activity?.reportFullyDrawn() // Safe call using ?.
            }
        }
    }
}

@Composable
fun GridRotateItem(index: Int, infiniteTransition: InfiniteTransition, onImageLoaded: () -> Unit) {
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    GridImage(index, Modifier.graphicsLayer(rotationZ = rotation), onImageLoaded)
}

@Composable
fun GridFadeItem(index: Int, infiniteTransition: InfiniteTransition, onImageLoaded: () -> Unit) {
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    GridImage(index, Modifier.alpha(alpha), onImageLoaded)
}

@Composable
fun GridScaleItem(index: Int, infiniteTransition: InfiniteTransition, onImageLoaded: () -> Unit) {
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    GridImage(index, Modifier.scale(scale), onImageLoaded)
}

@Composable
fun GridImage(index: Int, modifier: Modifier = Modifier, onImageLoaded: () -> Unit) {
    val columnWidthPx = LocalImageWidthPx.current
    val columnWidth = LocalImageWidth.current
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("file:///android_asset/images/${index % 20}.jpeg")
            .size(
                columnWidthPx,
                columnWidthPx
            ) // Equivalent to cacheHeight and cacheWidth in Flutterh
            .listener(onSuccess = { request, metadata ->
                onImageLoaded()
            })
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .aspectRatio(1f)
            .size(columnWidth.dp, columnWidth.dp)
    )
}
