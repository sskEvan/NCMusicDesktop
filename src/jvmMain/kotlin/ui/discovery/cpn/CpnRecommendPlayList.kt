package ui.discovery.cpn

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.ncmusic.ui.common.TableLayout
import http.NCRetrofitClient
import model.PlayListTab
import model.PlayListTabResult
import moe.tlaster.precompose.ui.viewModel
import ui.common.PaingFooterNumBar
import ui.common.ViewStateComponent
import ui.common.handleSuccess
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel

/**
 *  推荐歌单-更多
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CpnRecommendPlayList() {
    LazyColumn {
        // 精品歌单入口
        item {
            CpnHighQualityPlayListEntrance()
        }
        // 歌单标签bar
        stickyHeader {
            PlayListTabBar()
        }

        PlayList()

        item {
            PaingFooterNumBar(5) {

            }
        }
    }
}

@Composable
private fun PlayListTabBar() {
    Row(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 15.dp).fillMaxWidth()) {
        PlayListTabToggle()
//        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
//            HotPlayListTabs()
//        }
    }
    TabsPopup()
}

@Composable
private fun TabsPopup() {
    val viewModel = viewModel { CpnRecommendPlayListViewModel() }
    DropdownMenu(
        expanded = viewModel.showTabsPopup,
        onDismissRequest = {
            viewModel.showTabsPopup = false
        },
        offset = DpOffset(20.dp, 10.dp),
    ) {
        TabsPopupContent()
    }
}

@Composable
private fun TabsPopupContent() {

    val viewModel = viewModel { CpnRecommendPlayListViewModel() }
//    viewModel.playListTabFlow.value.handleSuccess { data ->
//        Column {
//            Box(
//                modifier = Modifier.padding(horizontal = 20.dp).height(40.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Text(text = data.all.name)
//            }
//            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = AppColorsProvider.current.divider)
//            val groupTabsMap = remember { viewModel.generateGroupTabsMap(data) }
//            groupTabsMap.forEach {
//                TabsPopupGroupTabsItem(it.key, it.value)
//            }
//        }
//    }
    ViewStateComponent(
        modifier = Modifier.width(660.dp).height(320.dp),
        initFlow = viewModel.playListTabFlow,
        key = "TabsPopupContent",
        loadDataBlock = { viewModel.getPlayListCategories() }
    ) { data ->
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier.padding(horizontal = 20.dp).height(40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = data.all.name)
            }
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = AppColorsProvider.current.divider)
            val groupTabsMap = remember { viewModel.generateGroupTabsMap(data) }
            groupTabsMap.forEach {
                TabsPopupGroupTabsItem(it.key, it.value)
            }
        }
    }
//    ViewStateComponent(
//        modifier = Modifier.width(600.dp).height(400.dp),
//        key = "TabsPopupContent",
//        loadDataBlock = { viewModel.getPlayListCategories() }
//    ) { data ->
//        Column {
//            Box(
//                modifier = Modifier.padding(horizontal = 20.dp).height(40.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Text(text = data.all.name)
//            }
//            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = AppColorsProvider.current.divider)
//            val groupTabsMap = remember { viewModel.generateGroupTabsMap(data) }
//            groupTabsMap.forEach {
//                TabsPopupGroupTabsItem(it.key, it.value)
//            }
//        }
//    }
}

@Composable
private fun TabsPopupGroupTabsItem(category: String, tabs: List<PlayListTab>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(category, modifier = Modifier.padding(end = 10.dp))
        TableLayout(cellsCount = 6, modifier = Modifier.fillMaxWidth()) {
            tabs.forEach {
                Text(text = it.name, modifier = Modifier.height(40.dp), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun PlayListTabToggle() {
    val viewModel = viewModel { CpnRecommendPlayListViewModel() }
    viewModel.selectedHotTab?.let { selectedHotTab ->
        Row(
            modifier = Modifier.padding(end = 20.dp).width(110.dp).height(30.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    viewModel.showTabsPopup = true
                }
                .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(50)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(selectedHotTab.name, color = AppColorsProvider.current.firstIcon, fontSize = 14.sp)

            Icon(
                painterResource("image/ic_more.webp"),
                modifier = Modifier.size(16.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.firstIcon
            )
        }
    }
}

@Composable
private fun HotPlayListTabs() {
    val viewModel = viewModel { CpnRecommendPlayListViewModel() }
    viewModel.hotTabFlow.collectAsState().value.handleSuccess { data ->
        LazyRow {
            items(data.tags.size) {
                HotPlayListTabItem(data.tags[it], it == data.tags.size - 1)
            }
        }
    }
}

@Composable
private fun HotPlayListTabItem(tag: PlayListTab, lastIndex: Boolean) {
    val viewModel = viewModel { CpnRecommendPlayListViewModel() }
    Row(
        modifier = Modifier.height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable {
                viewModel.selectedHotTab = tag
            }
            .let {
                if (tag.id == viewModel.selectedHotTab?.id) {
                    it.background(AppColorsProvider.current.primary.copy(0.2f))
                } else {
                    it
                }.padding(horizontal = 6.dp, vertical = 3.dp)
            }) {
            Text(
                tag.name,
                color = if (tag.id == viewModel.selectedHotTab?.id) AppColorsProvider.current.primary else AppColorsProvider.current.secondText,
                fontSize = 12.sp
            )
        }
        if (!lastIndex) {
            Divider(
                modifier = Modifier.padding(horizontal = 6.dp).width(1.dp),
                thickness = 12.dp,
                color = AppColorsProvider.current.divider
            )
        }
    }
}


private fun LazyListScope.PlayList() {

}

class CpnRecommendPlayListViewModel : BaseViewModel() {
    var selectedHotTab by mutableStateOf<PlayListTab?>(null)
    var showTabsPopup by mutableStateOf(false)

    val hotTabFlow by lazy {
        launch {
            NCRetrofitClient.getNCApi().getHotPlayListCategories()
        }
    }

    val playListTabFlow = getPlayListCategories()

    fun getPlayListCategories() = launch(handleSuccessBlock = {
        if (selectedHotTab == null) {
            selectedHotTab = it.all
        }
    }) {
        NCRetrofitClient.getNCApi().getPlayListCategories()
    }

    fun generateGroupTabsMap(data: PlayListTabResult): Map<String, MutableList<PlayListTab>> {
        val categories = data.categories
        val groupTabsMap = hashMapOf<String, MutableList<PlayListTab>>()
        categories.forEach {
            groupTabsMap[it.value] = mutableListOf()
        }
        data.sub.forEach { tab ->
            val categoryId = tab.category
            val category = data.categories[categoryId]
            groupTabsMap[category]?.add(tab)
        }
        return groupTabsMap
    }

}


