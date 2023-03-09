package ui.discovery.cpn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.load_the_image.rememberImagePainter
import com.ssk.ncmusic.ui.common.TableLayout
import http.NCRetrofitClient
import model.RecommendPlayListItem
import moe.tlaster.precompose.ui.viewModel
import ui.common.CpnActionMore
import ui.common.ViewStateComponent
import ui.common.theme.AppColorsProvider
import util.StringUtil
import viewmodel.BaseViewModel

/**
 * 推荐歌单入口
 */
@Composable
fun CpnRecommandPlayListEntrance(onClickMore: () -> Unit) {
    val viewModel = viewModel { CpnRecommendPlayListEntranceViewModel() }

    Column {
        CpnActionMore("推荐歌单") {
            onClickMore.invoke()
        }

        ViewStateComponent(
            key = "CpnRecommandPlayListEntrance",
            loadDataBlock = { viewModel.getRecommendPlayList() }) { data ->
            Content(data.result)
        }
    }
}

@Composable
private fun Content(list: List<RecommendPlayListItem>) {
    TableLayout(modifier = Modifier.padding(horizontal = 10.dp), cellsCount = 5) {
        list.forEach {
            RecommendPlayListItem(it)
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RecommendPlayListItem(item: RecommendPlayListItem) {
    var focusState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
            focusState = true
        }.onPointerEvent(PointerEventType.Exit) {
            focusState = false
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                rememberImagePainter(item.picUrl),
                contentDescription = "",
                modifier = Modifier.size(140.dp).clip(RoundedCornerShape(6.dp))
            )

            Row(modifier = Modifier.padding(top = 6.dp, end = 6.dp).align(Alignment.TopEnd), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource("image/ic_play_count.webp"),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 6.dp).size(12.dp)
                )
                Text(
                    StringUtil.friendlyNumber(item.playCount),
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }

            if (focusState) {
                Icon(
                    painter = painterResource("image/ic_logo_play.webp"),
                    contentDescription = "",
                    modifier = Modifier.padding(bottom = 6.dp, end = 6.dp).size(32.dp).align(Alignment.BottomEnd),
                    tint = Color.White
                )
            }
        }

        Text(
            item.name,
            color = AppColorsProvider.current.firstText,
            fontSize = 12.sp,
            maxLines = 2,
            modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp).height(48.dp)
        )
    }
}

class CpnRecommendPlayListEntranceViewModel : BaseViewModel() {

    fun getRecommendPlayList() = launch {
        println("获取推荐歌单...")
        NCRetrofitClient.getNCApi().getRecommendPlayList(15)
    }

}