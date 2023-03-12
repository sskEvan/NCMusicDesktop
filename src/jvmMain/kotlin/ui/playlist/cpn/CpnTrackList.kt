package ui.playlist.cpn

import androidx.compose.foundation.background
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
            TrackItem(data.songs[it], it + 1)
        }
    }
}

@Composable
private fun TrackHeaderBar() {
    Row(modifier = Modifier.height(36.dp).fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
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
private fun TrackItem(songBean: SongBean, index: Int) {
    Row(
        modifier = Modifier.background(
            if (index % 2 == 0) Color.Transparent else AppColorsProvider.current.divider.copy(
                0.25f
            )
        ).height(36.dp).fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(4f)) {
            val num = if (index < 10) "0$index" else index.toString()
            Text(
                text = num,
                color = AppColorsProvider.current.thirdText,
                fontSize = 12.sp,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )

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
                text = songBean.name,
                color = AppColorsProvider.current.firstText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier.weight(2f),
            text = songBean.ar.getOrNull(0)?.name ?: "未知歌手",
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.weight(2f),
            text = songBean.al.name,
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = songBean.getSongTimeLength(),
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
            flow = launch {
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