package ui.discovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.ui.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
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
//    val selectedIndex = remember { mutableStateOf(0) }
    val viewModel = viewModel { DiscoveryPageViewModel() }
    Column {
        CommonTopBar {
            CommonTabLayout(
                selectedIndex = viewModel.selectedIndex.value,
                tabTexts = tabs,
                backgroundColor = AppColorsProvider.current.topBarColor,
                style = CommonTabLayoutStyle(modifier = Modifier.height(50.dp))
            ) {
                viewModel.selectedIndex.value = it
            }
        }

        when (viewModel.selectedIndex.value) {
            0 -> CpnPersonalRecommend(viewModel.selectedIndex)
            else -> TodoPage(tabs[viewModel.selectedIndex.value])
        }
    }

}

class DiscoveryPageViewModel :ViewModel() {
    val selectedIndex = mutableStateOf(0)
}


