package ui.play

import ui.common.onClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.MusicPlayController
import base.player.IPlayerListener
import base.player.PlayerStatus
import http.NCRetrofitClient
import model.LyricContributorBean
import model.LyricResult
import moe.tlaster.precompose.ui.viewModel
import ui.common.ViewStateComponent
import ui.common.theme.AppColorsProvider
import util.LyricUtil
import util.convertDp
import base.BaseViewModel

/**
 * 歌词组件
 */
@Composable
fun CpnLyric(modifier: Modifier) {

    MusicPlayController.curSongBean?.let { songBean ->
        val viewModel = viewModel { CpnLyricViewModel() }

        ViewStateComponent(modifier = modifier,
            key = "CpnLyric-${songBean.id}",
            loadDataBlock = {
                viewModel.getLyric(songBean.id)
            },
            customLoadingComponent = {
                ViewStateTip("加载歌词中...")
            },
            customEmptyComponent = {
                ViewStateTip("暂无歌词")
            },
            customFailComponent = { _, loadDataBlock ->
                ViewStateTip("加载歌词出错, 点击重试", loadDataBlock)
            },
            customErrorComponent = { _, loadDataBlock ->
                ViewStateTip("加载歌词出错, 点击重试", loadDataBlock)
            }
        ) {
            LyricList(it)
        }
    }
}

@Composable
fun LyricList(data: LyricResult) {
    var cpnLyricHeight by remember { mutableStateOf(0) }
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current
    val viewModel = viewModel { CpnLyricViewModel() }
    LaunchedEffect(viewModel.curLyricIndex) {
        if (viewModel.curLyricIndex >= 0) {
            lazyListState.animateScrollToItem(viewModel.curLyricIndex)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 100.dp)
            .fillMaxSize()
            .onGloballyPositioned {
                cpnLyricHeight = it.size.height
            }
            .drawWithContent {
                val paint = Paint().asFrameworkPaint()
                drawIntoCanvas {
                    val layerId: Int = it.nativeCanvas.saveLayer(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        paint
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            Pair(0f, Color.Transparent),
                            Pair(0.15f, Color.White),
                            Pair(0.85f, Color.White),
                            Pair(1f, Color.Transparent)
                        ),
                        blendMode = BlendMode.DstIn
                    )
                    it.nativeCanvas.restoreToCount(layerId)
                }
            },
        state = lazyListState,
        contentPadding = PaddingValues(vertical = (cpnLyricHeight * 0.4).convertDp(density))
    ) {

        itemsIndexed(viewModel.lyricModelList) { index, item ->
            LyricItem(index, item, viewModel)
        }
        item {
            LyricConstructorInfo(data.lyricUser, data.transUser)
        }
    }
}

@Composable
private fun ViewStateTip(tip: String, loadDataBlock: (() -> Unit)? = null) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onClick {
                loadDataBlock?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = tip, color = AppColorsProvider.current.secondText, fontSize = 16.sp)
    }
}


@Composable
private fun LyricConstructorInfo(transUser: LyricContributorBean?, lyricUser: LyricContributorBean?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        lyricUser?.let {
            Text(
                text = "歌词贡献者：${it.nickname}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AppColorsProvider.current.secondText,
            )
        }

        transUser?.let {
            Text(
                text = "翻译贡献者：${it.nickname}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AppColorsProvider.current.secondText,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun LyricItem(index: Int, lyricModel: LyricModel, viewModel: CpnLyricViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        lyricModel.lyric?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = if (viewModel.curLyricIndex == index) AppColorsProvider.current.primary else AppColorsProvider.current.secondText,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        lyricModel.tLyric?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                color = if (viewModel.curLyricIndex == index) AppColorsProvider.current.primary else AppColorsProvider.current.secondText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }
    }
}


class CpnLyricViewModel : BaseViewModel(), IPlayerListener {
    var curLyricIndex by mutableStateOf(-1)
    val lyricModelList = mutableListOf<LyricModel>()
    var curPlayPosition = 0

    init {
        MusicPlayController.mediaPlayer.addListener(this)
    }

    override fun onCleared() {
        MusicPlayController.mediaPlayer.removeListener(this)
        super.onCleared()
    }

    fun getLyric(id: Long) = launchFlow(handleSuccessBlock = {
        lyricModelList.clear()
        lyricModelList.addAll(LyricUtil.parse(it))
        curLyricIndex = lyricModelList.indexOfFirst { lyricModel ->
            curPlayPosition < lyricModel.time
        } - 1
    }) {
        curPlayPosition = 0
        NCRetrofitClient.getNCApi().getLyric(id)
    }

    override fun onStatusChanged(status: PlayerStatus) {
    }

    override fun onProgress(totalDuring: Int, currentPosition: Int, percentage: Float) {
        curPlayPosition = currentPosition
        curLyricIndex = lyricModelList.indexOfFirst {
            currentPosition < it.time
        } - 1
        if (currentPosition > (lyricModelList.lastOrNull()?.time ?: 0)) {
            curLyricIndex = lyricModelList.size - 1
        }
    }
}


data class LyricModel(
    val time: Long,
    val lyric: String? = null,
    var tLyric: String? = null
)