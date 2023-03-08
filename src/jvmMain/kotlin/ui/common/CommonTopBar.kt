package ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.common.theme.AppColorsProvider

@Composable
fun CommonTopBar(title: String = "", customerContent: (@Composable () -> Unit)? = null) {
    Box(
        modifier = Modifier.width(480.dp).height(50.dp).background(AppColorsProvider.current.topBarColor),
        contentAlignment = Alignment.CenterStart
    ) {
        if (customerContent != null) {
            customerContent?.invoke()
        } else {
            Text(
                title,
                color = AppColorsProvider.current.firstText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}