package ui.share

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.lt.load_the_image.rememberImagePainter
import model.PlaylistDetail
import router.NCNavigatorManager
import router.RouterUrls
import ui.common.theme.AppColorsProvider
import util.StringUtil

/**
 * 歌单item组件
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CpnPlayListItem(item: PlaylistDetail) {
    var focusState by remember { mutableStateOf(false) }
//    val navigator = rememberNavigator()
    val navigator = NCNavigatorManager.navigator

    Column(
        modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
            focusState = true
        }.onPointerEvent(PointerEventType.Exit) {
            focusState = false
        }.clickable {
            val url = "${RouterUrls.PLAY_LIST_DETAIL}?simplePlayListInfo=${Gson().toJson(item.convertToSimple())}"
            navigator.navigate(url)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                rememberImagePainter(item.coverImgUrl),
                contentDescription = "",
                modifier = Modifier.size(172.dp).clip(RoundedCornerShape(6.dp))
            )

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

//@Composable
//fun LazyListScope.CpnPlayList(data: List<RecommendPlayListItem>, cells: Int) {
//    val rows = remember { (data.size + cells - 1) / cells }
//    val groupList = remember {
//        val groupList = mutableListOf<MutableList<RecommendPlayListItem>>()
//        for (i in 0 until   rows - 1) {
//            val group = mutableListOf<RecommendPlayListItem>()
//            group.addAll(data.subList(i * cells, cells))
//            groupList.add(group)
//        }
//        val lastGroup = mutableListOf<RecommendPlayListItem>()
//        lastGroup.addAll(data.subList(cells * (rows - 1), data.size))
//        groupList.add(lastGroup)
//        groupList
//    }
//    items(groupList.size) {
//        CpnPlayListGridRow(groupList[it])
//    }
//}
//
//@Composable
//fun CpnPlayList(data: List<PlaylistBean>, cells: Int) {
//    val rows = remember { (data.size + cells - 1) / cells }
//    val groupList = remember {
//        val groupList = mutableListOf<MutableList<PlaylistBean>>()
//        for (i in 0 until   rows - 1) {
//            val group = mutableListOf<PlaylistBean>()
//            group.addAll(data.subList(i * cells, (i + 1) * cells))
//            groupList.add(group)
//        }
//        val lastGroup = mutableListOf<PlaylistBean>()
//        lastGroup.addAll(data.subList(cells * (rows - 1), data.size))
//        groupList.add(lastGroup)
//        groupList
//    }
//    Column {
//        groupList.forEach {
//            CpnPlayListGridRow(it)
//        }
//    }
//
//}
//
//@Composable
//fun CpnPlayListGridRow(data: List<PlaylistBean>) {
//    TableLayout(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), cellsCount = data.size) {
//        data.forEach {
//            CpnPlayListItem(it)
//        }
//    }
//}
