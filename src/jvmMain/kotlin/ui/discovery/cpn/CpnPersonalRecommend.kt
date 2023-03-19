package ui.discovery.cpn

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.ui.viewModel
import base.BaseViewModel

/**
 * 个性推荐
 */
@Composable
fun CpnPersonalRecommendContainer(recommendTagIndex: MutableState<Int>) {
    val personalRecommendViewModel = viewModel { PersonalRecommendViewModel() }
    val playListEntranceViewModel = viewModel { RecommendPlayListEntranceViewModel() }
    val privateContentViewModel = viewModel { PrivateContentViewModel() }
    val newSongViewModel = viewModel { NewSongEntranceViewModel() }
    val recommendMVEntranceViewModel = viewModel { RecommendMVEntranceViewModel() }

    LaunchedEffect(Unit) {
        playListEntranceViewModel.getRecommendPlayList(true)
        privateContentViewModel.getPrivateContent(true)
        newSongViewModel.getNewSong(true)
        recommendMVEntranceViewModel.getRecommendMV(true)
    }
    val playListEntranceViewState = playListEntranceViewModel.flow?.collectAsState()?.value
    val privateContentViewState = privateContentViewModel.flow?.collectAsState()?.value
    val newSongViewState = newSongViewModel.flow?.collectAsState()?.value
    val recommendMVViewState = recommendMVEntranceViewModel.flow?.collectAsState()?.value

    LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp),
        state = personalRecommendViewModel.getLazyListStateState(rememberLazyListState())
    ) {
        // 推荐歌单
        CpnRecommandPlayListEntrance(playListEntranceViewModel, playListEntranceViewState) {
            recommendTagIndex.value = 1
        }

        // 独家放送
        CpnPrivateContentEntrance(privateContentViewModel, privateContentViewState)


        // 最新音乐
        CpnNewSongEntrance(newSongViewModel, newSongViewState)

        // 推荐MV
        CpnRecommendMVEntrance(recommendMVEntranceViewModel, recommendMVViewState)
    }
}


class PersonalRecommendViewModel : BaseViewModel() {
    private var lazyListState: LazyListState? = null

    fun getLazyListStateState(state: LazyListState): LazyListState {
        if (lazyListState == null) {
            lazyListState = state
        }
        return lazyListState!!
    }
}

