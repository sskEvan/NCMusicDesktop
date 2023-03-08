package ui.main.cpn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import router.NavGraph
import ui.common.CommonTopBar
import ui.common.theme.AppColorsProvider


@Composable
fun CpnMainRightContainer() {
    Box(modifier = Modifier.fillMaxSize().background(color = AppColorsProvider.current.pure)) {
        CommonTopBar()
        NavGraph()
        CpnRightTopActionButtons()
    }
}


@Composable
private fun BoxScope.CpnRightTopActionButtons() {
    val showPopupWindow = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(50.dp).width(320.dp).background(AppColorsProvider.current.topBarColor)
            .align(Alignment.TopEnd),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource("image/ic_setting.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(24.dp).padding(3.dp),
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_message.webp"),
            contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(24.dp).padding(3.dp),
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_theme.webp"),
            contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(24.dp).padding(3.dp).clickable {
                showPopupWindow.value = true
            },
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_screen_min.webp"),
            contentDescription = null,
            modifier = Modifier.padding(end = 24.dp).size(24.dp).padding(3.dp),
            tint = AppColorsProvider.current.firstIcon
        )
    }

    CpnThemePopup(showPopupWindow)
}