package ui.discovery.cpn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.CpnActionMore
import ui.common.theme.AppColorsProvider

/**
 * 个性推荐
 */
@Composable
fun CpnPersonalRecommend(recommendTagIndex: MutableState<Int>) {
    Column(modifier = Modifier.background(AppColorsProvider.current.pure).verticalScroll(rememberScrollState())) {
        // 推荐歌单
        CpnRecommandPlayListEntrance {
            recommendTagIndex.value = 1
        }
        // 独家放送
        CpnPrivateContentEntrance {

        }
        // 最新音乐
        CpnNewSongEntrance {

        }
        // 推荐MV
        CpnRecommendMVEntrance {

        }
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


