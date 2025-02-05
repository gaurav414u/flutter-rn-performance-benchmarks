package com.example.kmp_list_view_perf

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kmp_list_view_perf.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
@Preview
fun App() {
    MaterialTheme {
        TestScreen()
    }
}

val LocalItemHeight = staticCompositionLocalOf<Float> { error("Item height not provided") }

data class Item(val index: Int, val color: Color)


@Composable
fun TestScreen() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val data = remember { generateData() }

    val itemHeight = with(
        androidx.compose.ui.platform.LocalDensity.current
    ) { 100.dp.toPx() }
    val spacerHeight = with(
        androidx.compose.ui.platform.LocalDensity.current
    ) { 20.dp.toPx() }

    CompositionLocalProvider(LocalItemHeight provides itemHeight) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(20.dp) // Adjust spacing as needed
            ) {
                items(data) { item ->
                    Cell(item)
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollBy(
                            1000f * itemHeight,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    1000.toDuration(DurationUnit.SECONDS).inWholeMilliseconds.toInt(),
                                    easing = LinearEasing
                                ),
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Start Scrolling")
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Cell(item: Item) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(item.color)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(Res.getUri("drawable/${item.index % 20}.jpeg"))
                .size(
                    LocalItemHeight.current.toInt(),
                    LocalItemHeight.current.toInt()
                )
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1f)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(Res.getUri("drawable/${item.index % 20}.jpeg"))
                .size(
                    LocalItemHeight.current.toInt(),
                    LocalItemHeight.current.toInt()
                )
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer(rotationZ = rotation)
                .aspectRatio(1f)
        )
        Text(
            text = item.index.toString(), modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

fun generateData(): List<Item> {
    return List(1001) { index -> Item(index, randomColor()) }
}

fun randomColor(): Color {
    return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
}
