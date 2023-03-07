package ui.discovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.CommonTabLayout
import ui.common.CommonTabLayoutStyle
import ui.todo.TodoPage

@Composable
fun DiscoveryPage() {
    val tabs = remember {
        listOf("个性推荐", "歌单", "排行榜", "歌手", "最新音乐")
    }
    var selectedIndex by remember { mutableStateOf(0) }

    Column {
        CommonTabLayout(
            selectedIndex = selectedIndex,
            tabTexts = tabs,
            style = CommonTabLayoutStyle(modifier = Modifier.height(56.dp))
        ) {
            selectedIndex = it
        }
        TodoPage(tabs[selectedIndex])
    }

}


