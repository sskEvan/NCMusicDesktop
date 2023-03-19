package ui.discovery.cpn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import http.NCRetrofitClient
import model.PlayListResult
import model.PlaylistDetail
import moe.tlaster.precompose.ui.viewModel
import ui.common.AsyncImage
import ui.common.handleSuccess
import ui.common.theme.AppColorsProvider
import base.BaseViewModel
import base.ViewStateMutableStateFlow

/**
 * 个性推荐-精品歌单-入口
 */
@Composable
fun CpnHighQualityPlayListEntrance(tag: String) {
    val highQualityPlayListEntranceViewModel = viewModel { HighQualityPlayListEntranceViewModel() }
    val flow = remember(tag) {
        highQualityPlayListEntranceViewModel.getHighQualityPlayList(tag)
    }
    Box(
        modifier = Modifier.fillMaxWidth().height(200.dp)
            .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(6.dp))
            .background(AppColorsProvider.current.divider.copy(0.2f), RoundedCornerShape(6.dp))
    ) {
        flow?.collectAsState()?.value?.handleSuccess {
            Content(it.playlists.getOrNull(0))
        }
    }

}

@Composable
private fun Content(playlistBean: PlaylistDetail?) {
    playlistBean?.let {
        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))) {
            AsyncImage(
                modifier = Modifier.fillMaxSize().blur(80.dp),
                playlistBean.coverImgUrl,
            )

            Row(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    modifier = Modifier.padding(end = 20.dp).size(130.dp).clip(RoundedCornerShape(6.dp)),
                    playlistBean.coverImgUrl
                )

                Column {
                    Row(
                        modifier = Modifier.width(100.dp).height(30.dp)
                            .border(BorderStroke(1.dp, color = Color(0xFFD8B839)), RoundedCornerShape(50)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource("image/ic_queue.webp"),
                            modifier = Modifier.padding(end = 6.dp).size(16.dp),
                            contentDescription = "",
                            tint = Color(0xFFD8B839)
                        )
                        Text("精品歌单", color = Color(0xFFD8B839), fontSize = 12.sp)
                    }

                    Text(
                        playlistBean.name,
                        color = AppColorsProvider.current.pure,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        playlistBean.description ?: "", color = AppColorsProvider.current.pure,
                        fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }
    }
}

class HighQualityPlayListEntranceViewModel : BaseViewModel() {
    var flow: ViewStateMutableStateFlow<PlayListResult>? = null
    var lastTag = ""
    fun getHighQualityPlayList(tag: String?): ViewStateMutableStateFlow<PlayListResult>? {
        if (lastTag != tag) {
            lastTag = tag ?: ""
            flow = launchFlow {
                println("----getHighQualityPlayList done, lastTag=${lastTag}")
                NCRetrofitClient.getNCApi().getHighQualityPlayList(1, tag)
            }
        }
        return flow
    }

}
