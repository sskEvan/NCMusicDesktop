package ui.discovery.cpn

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.*
import ui.common.PaingFooterNumBar

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
            CpnPlayListTabSelectedBar()
        }

        PlayList()

        item {
            PaingFooterNumBar(5) {
            }
        }
    }
}

private fun LazyListScope.PlayList() {

}


