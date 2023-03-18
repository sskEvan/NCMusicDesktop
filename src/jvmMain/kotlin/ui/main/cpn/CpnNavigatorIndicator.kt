package ui.main.cpn

import androidx.compose.foundation.background
import ui.common.onClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import router.NCNavigatorManager
import ui.common.theme.AppColorsProvider

@Composable
fun CpnNavigatorIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth().height(50.dp).background(AppColorsProvider.current.topBarColor),
        contentAlignment = Alignment.CenterEnd
    ) {
        println(
            "--------------------CpnNavigatorIndicator NCNavigatorManager.navigator currentEntry = ${NCNavigatorManager.navigator.currentEntry}" +
                    ", currentEntry = ${NCNavigatorManager.navigator.canGoBack}"
        )
        Icon(
            painterResource("image/ic_back.webp"),
            modifier = Modifier.padding(end = 20.dp).clip(RoundedCornerShape(50)).let {
                if (NCNavigatorManager.navigator.canGoBack) {
                    it.onClick  {
                        NCNavigatorManager.navigator.popBackStack()
                    }
                } else {
                    it
                }
            }.padding(5.dp).size(16.dp),
            contentDescription = "返回上一页",
            tint = if (NCNavigatorManager.navigator.canGoBack) AppColorsProvider.current.firstIcon else AppColorsProvider.current.thirdIcon
        )

    }

}