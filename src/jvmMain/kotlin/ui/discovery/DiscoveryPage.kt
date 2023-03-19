package ui.discovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import base.MusicPlayController
import moe.tlaster.precompose.ui.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
import router.NCNavigatorManager
import ui.common.CommonTabLayout
import ui.common.CommonTabLayoutStyle
import ui.common.theme.AppColorsProvider
import ui.discovery.cpn.CpnPersonalRecommendContainer
import ui.discovery.cpn.CpnRecommendPlayList
import ui.main.cpn.CommonTitleBar
import ui.todo.TodoPage

/**
 * 发现音乐页面
 */
@Composable
fun DiscoveryPage() {
    val tabs = remember {
        listOf("个性推荐", "歌单", "排行榜", "歌手", "最新音乐")
    }
    val viewModel = viewModel { DiscoveryPageViewModel() }
    Column {
        CommonTitleBar {
            CommonTabLayout(
                selectedIndex = viewModel.selectedIndex.value,
                tabTexts = tabs,
                backgroundColor = if (MusicPlayController.showMusicPlayDrawer) AppColorsProvider.current.pure else AppColorsProvider.current.topBarColor,
                style = CommonTabLayoutStyle(modifier = Modifier.height(50.dp))
            ) {
                viewModel.selectedIndex.value = it
            }
        }

        when (viewModel.selectedIndex.value) {
            0 -> CpnPersonalRecommendContainer(viewModel.selectedIndex)
            1 -> CpnRecommendPlayList()
            else -> TodoPage(tabs[viewModel.selectedIndex.value], false)
        }
    }

}

class DiscoveryPageViewModel : ViewModel() {
    val selectedIndex = mutableStateOf(0)
}


