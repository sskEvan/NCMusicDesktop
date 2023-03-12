package util

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Number.convertDp(density: Density): Dp {
    return (toFloat() / density.density).dp
}