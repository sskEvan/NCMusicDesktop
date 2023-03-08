package ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.common.theme.AppColorsProvider

@Composable
fun CpnActionMore(title: String, onClickMore: (() -> Unit) ?= null) {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp)
            .clickable {
                onClickMore?.invoke()
            }
            .padding(start = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppColorsProvider.current.firstText)
        Icon(
            painterResource("image/ic_more.webp"),
            contentDescription = "更多",
            modifier = Modifier.size(16.dp)
        )
    }
}