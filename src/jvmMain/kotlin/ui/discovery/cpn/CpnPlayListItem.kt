package ui.discovery.cpn

import androidx.compose.foundation.Image
import ui.common.onClick
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
import com.google.gson.Gson
import model.PlaylistDetail
import moe.tlaster.precompose.navigation.NavOptions
import router.NCNavigatorManager
import router.RouterUrls
import ui.common.AsyncImage
import ui.common.theme.AppColorsProvider
import util.StringUtil

/**
 * 歌单item组件
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CpnPlayListItem(item: PlaylistDetail) {
    var focusState by remember { mutableStateOf(false) }
    val navigator = NCNavigatorManager.navigator

    Column(
        modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
            focusState = true
        }.onPointerEvent(PointerEventType.Exit) {
            focusState = false
        }.onClick  {
            val url = "${RouterUrls.PLAY_LIST_DETAIL}?simplePlayListInfo=${Gson().toJson(item.convertToSimple())}"
            navigator.navigate(url)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(modifier = Modifier.size(172.dp).clip(RoundedCornerShape(6.dp)), item.coverImgUrl)

            Row(
                modifier = Modifier.padding(top = 6.dp, end = 6.dp).align(Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

