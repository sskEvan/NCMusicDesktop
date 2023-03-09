package ui.discovery.cpn

import androidx.compose.foundation.ScrollState
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
import moe.tlaster.precompose.ui.viewModel
import ui.common.CpnActionMore
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel

/**
 * 个性推荐
 */
@Composable
fun CpnPersonalRecommend(recommendTagIndex: MutableState<Int>) {
    val viewModel = viewModel { CpnPersonalRecommend() }
    Column(
        modifier = Modifier.background(AppColorsProvider.current.pure).verticalScroll(
            viewModel.getCacheScrollState(rememberScrollState())
        )
    ) {
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


class CpnPersonalRecommend : BaseViewModel() {
    private var cacheScrollState: ScrollState? = null

    fun getCacheScrollState(scrollState: ScrollState): ScrollState {
        if (cacheScrollState == null) {
            cacheScrollState = scrollState
        }
        return cacheScrollState!!
    }
}

