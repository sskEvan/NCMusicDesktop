package ui.discovery.cpn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.load_the_image.rememberImagePainter
import com.ssk.ncmusic.ui.common.TableLayout
import http.NCRetrofitClient
import model.NewSongBean
import moe.tlaster.precompose.ui.viewModel
import ui.common.CpnActionMore
import ui.common.ViewStateComponent
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel

/**
 * 最新音乐入口
 */
@Composable
fun CpnNewSongEntrance(onClickMore: () -> Unit) {
    val viewModel = viewModel { CpnNewSongEntranceViewModel() }

    Column {
        CpnActionMore("最新音乐") {
            onClickMore.invoke()
        }

        ViewStateComponent(
            key = "CpnNewSongEntrance",
            loadDataBlock = { viewModel.getNewSong() }) { data ->
            Content(data.data)
        }
    }
}

@Composable
private fun Content(list: List<NewSongBean>) {
    TableLayout(modifier = Modifier.padding(horizontal = 10.dp), cellsCount = 2) {
        list.forEachIndexed { index, item ->
            NewSongItem(index + 1, item)
        }
    }
}


@Composable
private fun NewSongItem(index: Int, item: NewSongBean) {


    Column(
        modifier = Modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.Center
    ) {
        Divider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            thickness = 0.5.dp,
            color = AppColorsProvider.current.divider
        )
        Row(
            modifier = Modifier.padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    rememberImagePainter(item.album.picUrl),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(6.dp))
                )

                Icon(
                    painter = painterResource("image/ic_logo_play.webp"),
                    contentDescription = "",
                    modifier = Modifier.size(28.dp).align(Alignment.Center),
                    tint = Color.White
                )
            }

            val num = if (index < 10) "0$index" else "$index"
            Text(num, color = AppColorsProvider.current.thirdText, fontSize = 12.sp, modifier = Modifier.padding(12.dp))

            Column {
                Text(
                    item.name,
                    color = AppColorsProvider.current.firstText,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    item.artists[0].name,
                    color = AppColorsProvider.current.secondText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

        }
        Divider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            thickness = 0.5.dp,
            color = AppColorsProvider.current.divider
        )
    }
}

class CpnNewSongEntranceViewModel : BaseViewModel() {

    fun getNewSong() = launchFlow(handleSuccessBlock = {
        it.data = it.data.take(10)
    }) {
        println("获取新歌速递...")
        NCRetrofitClient.getNCApi().getNewSong()
    }

}