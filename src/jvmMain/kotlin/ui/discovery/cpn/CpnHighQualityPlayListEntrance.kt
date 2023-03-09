package ui.discovery.cpn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.load_the_image.rememberImagePainter
import http.NCRetrofitClient
import model.PlaylistBean
import moe.tlaster.precompose.ui.viewModel
import ui.common.handleSuccess
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel

/**
 * 精品歌单-入口
 */
@Composable
fun CpnHighQualityPlayListEntrance() {
    val highQualityPlayListEntranceViewModel = viewModel { CpnHighQualityPlayListEntranceViewModel() }
    val playListTabSelectedBarViewModel = viewModel { CpnPlayListTabSelectedBarViewModel() }
    val flow = remember(playListTabSelectedBarViewModel.selectedHotTab?.name ?: "全部歌单") {
        highQualityPlayListEntranceViewModel.getHighQualityPlayList(playListTabSelectedBarViewModel.selectedHotTab?.name)
    }
    flow.collectAsState().value.handleSuccess {
        Content(it.playlists[0])
    }
}

@Composable
private fun Content(playlistBean: PlaylistBean) {
    Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp).fillMaxWidth().clip(RoundedCornerShape(6.dp))) {
        // 高斯模糊
        Image(
            rememberImagePainter(playlistBean.coverImgUrl, placeholderResource = "image/ic_disk_place_holder.webp"),
            modifier = Modifier.fillMaxWidth().height(160.dp).blur(80.dp),
            contentScale = ContentScale.FillBounds,
            contentDescription = ""
        )

        Row(
            modifier = Modifier.fillMaxWidth().height(160.dp).padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                rememberImagePainter(playlistBean.coverImgUrl, placeholderResource = "image/ic_disk_place_holder.webp"),
                modifier = Modifier.padding(end = 20.dp).size(130.dp).clip(RoundedCornerShape(6.dp)),
                contentDescription = ""
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

class CpnHighQualityPlayListEntranceViewModel : BaseViewModel() {
    fun getHighQualityPlayList(tag: String?) = launch {
        NCRetrofitClient.getNCApi().getHighQualityPlayList(1, tag)
    }
}
