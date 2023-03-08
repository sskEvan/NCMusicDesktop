package ui.discovery.cpn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import http.NCRetrofitClient
import moe.tlaster.precompose.ui.viewModel
import ui.common.CpnActionMore
import ui.common.ViewStateComponent
import ui.common.theme.AppColorsProvider
import ui.common.theme.THEME_BLUE
import ui.common.theme.THEME_DEFAULT
import ui.common.theme.themeTypeState
import viewmodel.BaseViewModel

/**
 * 个性推荐
 */
@Composable
fun CpnPersonalRecommend(recommendTagIndex: MutableState<Int>) {
    Column(modifier = Modifier.background(AppColorsProvider.current.pure).verticalScroll(rememberScrollState())) {
        // 推荐歌单
        CpnRecommandPlayList {
            recommendTagIndex.value = 1
        }
        CpnTodo("独家放送")
        CpnTodo("最新音乐")
        CpnTodo("推荐MV")
        CpnTodo("播客")
    }
}

@Composable
fun ColumnScope.CpnTodo(title: String) {
    CpnActionMore(title)
    Box(
        Modifier.fillMaxWidth().height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("TODO-$title", color = AppColorsProvider.current.firstText)
    }
}


