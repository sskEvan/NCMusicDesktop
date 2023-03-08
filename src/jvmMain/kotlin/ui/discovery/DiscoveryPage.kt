package ui.discovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.CommonTabLayout
import ui.common.CommonTabLayoutStyle
import ui.common.theme.AppColorsProvider
import ui.discovery.cpn.CpnPersonalRecommend
import ui.main.cpn.CommonTopBar
import ui.todo.TodoPage

@Composable
fun DiscoveryPage() {
    val tabs = remember {
        listOf("个性推荐", "歌单", "排行榜", "歌手", "最新音乐")
    }
    val selectedIndex = remember { mutableStateOf(0) }

    Column {
        CommonTopBar {
            CommonTabLayout(
                selectedIndex = selectedIndex.value,
                tabTexts = tabs,
                backgroundColor = AppColorsProvider.current.topBarColor,
                style = CommonTabLayoutStyle(modifier = Modifier.height(50.dp))
            ) {
                selectedIndex.value = it
            }
        }

        when (selectedIndex.value) {
            0 -> CpnPersonalRecommend(selectedIndex)
            else -> TodoPage(tabs[selectedIndex.value])
        }
    }

}


