package ui.main.cpn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import ui.common.onClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import base.AppConfig
import base.MusicPlayController
import moe.tlaster.precompose.ui.viewModel
import ui.common.theme.AppColorsProvider
import ui.play.CpnLyric
import ui.play.CpnMusicPlay
import ui.play.CpnSongInfo
import ui.playlist.cpn.CpnCommentList
import ui.playlist.cpn.CommentListViewModel

/**
 * 音乐播放抽屉组件
 */
@Composable
fun CpnMainMusicPlayDrawer() {
    Box {
        if (MusicPlayController.showMusicPlayDrawer) {
            Box(
                modifier = Modifier.height(AppConfig.topBarHeight).width(200.dp),
            ) {
                Icon(
                    painterResource("image/ic_back.webp"),
                    modifier = Modifier.padding(start = 72.dp, top = 4.dp).clip(RoundedCornerShape(50))
                        .background(AppColorsProvider.current.secondary.copy(0.05f))
                        .onClick  {
                            MusicPlayController.showMusicPlayDrawer = false
                        }.padding(4.dp).size(12.dp).rotate(270f),
                    contentDescription = "返回上一页",
                    tint = AppColorsProvider.current.thirdIcon

                )
            }
        }

        AnimatedVisibility(
            MusicPlayController.showMusicPlayDrawer,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {

            MusicPlayController.curSongBean?.let { songBean ->
                val commentViewModel = viewModel(keys = listOf(songBean.id)) { CommentListViewModel() }
                LaunchedEffect(songBean.id) {
                    commentViewModel.fetchDataPaging("music", songBean.id, 1, true)
                }
                val commentViewState = commentViewModel.flow?.collectAsState()

                LazyColumn(
                    modifier = Modifier.padding(top = AppConfig.topBarHeight).background(AppColorsProvider.current.pure).padding(horizontal = 30.dp)
                ) {
                    item {
                        Header()
                    }

                    CpnCommentList(commentViewState?.value, commentViewModel) { curPage ->
                        commentViewModel.fetchDataPaging("music", songBean.id, curPage)
                    }
                }
            }
        }
    }
}

@Composable
private fun Header() {
    val height = remember { AppConfig.windowMinHeight - AppConfig.topBarHeight - 90.dp }
    Row(modifier = Modifier.fillMaxWidth().height(height)) {
        CpnMusicPlay(Modifier.weight(1f).fillMaxHeight())
        Column(Modifier.padding(end = 80.dp).weight(1f).fillMaxHeight()) {
            CpnSongInfo()
            CpnLyric(Modifier)
        }
    }
}

