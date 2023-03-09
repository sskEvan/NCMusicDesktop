package ui.discovery.cpn

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.ncmusic.ui.common.TableLayout
import http.NCRetrofitClient
import model.PlayListTab
import model.PlayListTabResult
import moe.tlaster.precompose.ui.viewModel
import ui.common.ViewStateComponent
import ui.common.handleSuccess
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel

@Composable
fun CpnPlayListTabSelectedBar() {
    val viewModel = viewModel { CpnPlayListTabSelectedBarViewModel() }
    Row(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 15.dp).fillMaxWidth()) {
        PlayListTabToggle(viewModel)
        HotPlayListTabs(viewModel)
    }
    TabsPopup(viewModel)
}


@Composable
private fun TabsPopup(viewModel: CpnPlayListTabSelectedBarViewModel) {
    DropdownMenu(
        expanded = viewModel.showTabsPopup,
        onDismissRequest = {
            viewModel.showTabsPopup = false
        },
        offset = DpOffset(20.dp, 10.dp),
    ) {
        TabsPopupContent(viewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsPopupContent(viewModel: CpnPlayListTabSelectedBarViewModel) {

    ViewStateComponent(modifier = Modifier.width(660.dp).height(320.dp),
        initFlow = viewModel.playListTabFlow,
        key = "TabsPopupContent",
        loadDataBlock = { viewModel.getPlayListCategories() }) { data ->

        val groupTabsMap = remember { viewModel.generateGroupTabsMap(data) }
        LazyColumn {
            stickyHeader {
                PlayListTabItem(
                    modifier = Modifier.padding(start = 20.dp, bottom = 15.dp, top = 6.dp).height(32.dp),
                    viewModel = viewModel,
                    textSize = 13.sp,
                    tag = data.all
                )
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = AppColorsProvider.current.divider)
            }
            item {
                groupTabsMap.forEach {
                    TabsPopupGroupTabsItem(viewModel, it.key, it.value)
                }
            }
        }
    }
}

@Composable
private fun TabsPopupGroupTabsItem(
    viewModel: CpnPlayListTabSelectedBarViewModel, category: String, tabs: List<PlayListTab>
) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 10.dp)) {
        Text(
            category,
            modifier = Modifier.padding(end = 20.dp, top = 10.dp),
            color = AppColorsProvider.current.thirdText,
            fontSize = 12.sp
        )
        TableLayout(cellsCount = 6, modifier = Modifier.fillMaxWidth()) {
            tabs.forEach {
                PlayListTabItem(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp).height(30.dp),
                    viewModel = viewModel,
                    tag = it
                )
            }
        }
    }
}

@Composable
private fun PlayListTabToggle(viewModel: CpnPlayListTabSelectedBarViewModel) {
    Row(
        modifier = Modifier.padding(end = 20.dp).width(110.dp).height(30.dp).clip(RoundedCornerShape(50)).clickable {
            viewModel.showTabsPopup = true
        }.border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(50)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            viewModel.selectedHotTab?.name ?: "选择标签", color = AppColorsProvider.current.firstIcon, fontSize = 14.sp
        )

        Icon(
            painterResource("image/ic_more.webp"),
            modifier = Modifier.size(16.dp),
            contentDescription = "",
            tint = AppColorsProvider.current.firstIcon
        )
    }
}

@Composable
private fun PlayListTabItem(
    modifier: Modifier,
    textSize: TextUnit = 12.sp,
    viewModel: CpnPlayListTabSelectedBarViewModel,
    tag: PlayListTab
) {

    Box(modifier = modifier.clip(RoundedCornerShape(50)).clickable {
        viewModel.selectedHotTab = tag
        viewModel.showTabsPopup = false
    }.let {
        if (tag.name == viewModel.selectedHotTab?.name) {
            it.background(AppColorsProvider.current.primary.copy(0.2f))
        } else {
            it
        }.padding(horizontal = 6.dp, vertical = 3.dp)
    }, contentAlignment = Alignment.Center
    ) {
        Row {
            Text(
                tag.name,
                color = if (tag.name == viewModel.selectedHotTab?.name) AppColorsProvider.current.primary else AppColorsProvider.current.firstText,
                fontSize = textSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (tag.hot) {
                Icon(
                    painterResource("image/ic_hot.webp"),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 2.dp).size(14.dp),
                    tint = AppColorsProvider.current.primary
                )

            }
        }
    }

}

@Composable
private fun RowScope.HotPlayListTabs(viewModel: CpnPlayListTabSelectedBarViewModel) {
    viewModel.hotTabFlow.collectAsState().value.handleSuccess { data ->
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            LazyRow {
                items(data.tags.size) {
                    HotPlayListTabItem(viewModel, data.tags[it], it == data.tags.size - 1)
                }
            }
        }
    }
}

@Composable
private fun HotPlayListTabItem(viewModel: CpnPlayListTabSelectedBarViewModel, tag: PlayListTab, lastIndex: Boolean) {
    Row(
        modifier = Modifier.height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.clip(RoundedCornerShape(50)).clickable {
            viewModel.selectedHotTab = tag
        }.let {
            if (tag.name == viewModel.selectedHotTab?.name) {
                it.background(AppColorsProvider.current.primary.copy(0.2f))
            } else {
                it
            }.padding(horizontal = 6.dp, vertical = 3.dp)
        }) {
            Text(
                tag.name,
                color = if (tag.name == viewModel.selectedHotTab?.name) AppColorsProvider.current.primary else AppColorsProvider.current.secondText,
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


class CpnPlayListTabSelectedBarViewModel : BaseViewModel() {
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


