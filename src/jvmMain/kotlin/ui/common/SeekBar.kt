package ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import ui.common.theme.AppColorsProvider

/**
 * Created by ssk on 2022/4/23.
 */
private val progressPaint = Paint().apply {
    isAntiAlias = true
    style = PaintingStyle.Fill
}

private val circlePaint = Paint().apply {
    isAntiAlias = true
    style = PaintingStyle.Fill
}

var width = 0f
var height = 0f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SeekBar(
    progress: Float = 0f,
    enableSeek: Boolean = false,
    seeking: (Float) -> Unit = {},
    seekTo: (Float) -> Unit = {},
    smallRadius: Float = 8.dp.value,
    largeRadius: Float = 12.dp.value,
    progressHeight: Float = 4.dp.value,
    seekBarColor: Color = Color.LightGray.copy(0.3f),
    progressColor: Color = AppColorsProvider.current.primary,
    circleColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {

    var isPressed by remember {
        mutableStateOf(false)
    }

    var circleCenterX by remember {
        mutableStateOf(0f)
    }

    circlePaint.color = circleColor

    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Press) {
                if (enableSeek) {
                    isPressed = true
                    val x = it.changes.first().position.x
                    seeking.invoke(x * 100f / width)
                }
            }
            .onPointerEvent(PointerEventType.Move) {
                if (isPressed) {
                    val x = it.changes.first().position.x
                    circleCenterX = x

                    if (x < 0f) {
                        circleCenterX = 0f
                    } else if (x > width) {
                        circleCenterX = width
                    } else {
                        circleCenterX = x
                    }
                    seeking.invoke(circleCenterX * 100f / width)
                }
            }
            .onPointerEvent(PointerEventType.Release) {
                if (enableSeek) {
                    seekTo.invoke(circleCenterX * 100f / width)
                    isPressed = false
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            width = drawContext.size.width
            height = drawContext.size.height
            drawIntoCanvas {
                progressPaint.color = seekBarColor
                val seekBarRect = Rect(
                    Offset(0f, (height - progressHeight) / 2),
                    Offset(width, (height + progressHeight) / 2)
                )
                it.drawRect(seekBarRect, progressPaint)

                progressPaint.color = progressColor
                val progressRect = Rect(
                    Offset(0f, (height - progressHeight) / 2),
                    Offset(width * progress / 100, (height + progressHeight) / 2)
                )
                it.drawRect(progressRect, progressPaint)

                var x = width * progress / 100
                val radius = if (isPressed) largeRadius else smallRadius
                if (x < radius) {
                    x = radius
                } else if (x > width - radius) {
                    x = width - radius
                }
                it.drawCircle(
                    Offset(x, height / 2),
                    radius,
                    circlePaint
                )
            }
        }
    }
}

