package ui.playlist.cpn

import androidx.compose.foundation.background
import ui.common.onClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.MusicPlayController
import http.NCRetrofitClient
import model.PlaylistDetailResult
import model.SongBean
import model.SongDetailResult
import ui.common.handleListContent
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel
import viewmodel.ViewState
import viewmodel.ViewStateMutableStateFlow

fun LazyListScope.CpnTrackList(
    viewState: ViewState<SongDetailResult>?,
    reloadCallback: () -> Unit
) {
    handleListContent(viewState,
        reloadDataBlock = {
            reloadCallback.invoke()
        }) { data ->
        item {
            TrackHeaderBar()
        }
        items(data.songs.size) {
            TrackItem(data.songs, it)
        }
    }
}

@Composable
private fun TrackHeaderBar() {
    Row(
        modifier = Modifier.height(36.dp).fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(4f),
            text = "音乐标题",
            color = AppColorsProvider.current.thirdText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(2f),
            text = "歌手",
            color = AppColorsProvider.current.thirdText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(2f),
            text = "专辑",
            color = AppColorsProvider.current.thirdText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "时长",
            color = AppColorsProvider.current.thirdText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun TrackItem(songList: List<SongBean>,  index: Int) {
    val curSongBean = songList[index]
    Row(
        modifier = Modifier
            .onClick  {
                MusicPlayController.setDataSource(songList, index)
            }
            .background(
            if ((index + 1) % 2 == 0) Color.Transparent else AppColorsProvider.current.divider.copy(
                0.25f
            )
        ).height(36.dp).fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(4f).fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            val num = if (index + 1 < 10) "0${index + 1}" else (index + 1).toString()
            if (MusicPlayController.curSongBean?.id != curSongBean.id) {
                Text(
                    text = num,
                    color = AppColorsProvider.current.thirdText,
                    fontSize = 12.sp,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Icon(
                    painterResource("image/ic_playing.webp"),
                    contentDescription = null,
                    modifier = Modifier.width(40.dp).height(36.dp).padding(horizontal = 14.dp, vertical = 12.dp),
                    tint = AppColorsProvider.current.primary
                )
            }

            Icon(
                painter = painterResource("image/ic_like.webp"),
                modifier = Modifier.padding(end = 8.dp).size(14.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.secondText
            )

            Icon(
                painter = painterResource("image/ic_download.webp"),
                modifier = Modifier.padding(end = 10.dp).size(14.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.secondIcon
            )

            Text(
                text = curSongBean.name,
                color = AppColorsProvider.current.firstText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier.weight(2f),
            text = curSongBean.ar.getOrNull(0)?.name ?: "未知歌手",
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.weight(2f),
            text = curSongBean.al.name,
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = curSongBean.getSongTimeLength(),
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

class TrackListViewModel : BaseViewModel() {
    var flow by mutableStateOf<ViewStateMutableStateFlow<SongDetailResult>?>(null)
    var playlistDetailResult by mutableStateOf<PlaylistDetailResult?>(null)
    fun fetchData(id: Long, firstLoad: Boolean = false) {
        if (!firstLoad || flow == null) {
            flow = launchFlow {
                val playlistDetailResult = NCRetrofitClient.getNCApi().getPlaylistDetail(id)
                this.playlistDetailResult = playlistDetailResult
                val trackIdBeans = playlistDetailResult.playlist.trackIds
                val ids = StringBuilder()
                if (trackIdBeans != null) {
                    val size = trackIdBeans.size
                    for (i in 0 until size) {
                        //最后一个参数不加逗号
                        if (i == size - 1) {
                            ids.append(trackIdBeans[i].id)
                        } else {
                            ids.append(trackIdBeans[i].id).append(",")
                        }
                    }
                }
                NCRetrofitClient.getNCApi().getSongDetail(ids.toString())
            }
        }
    }
}