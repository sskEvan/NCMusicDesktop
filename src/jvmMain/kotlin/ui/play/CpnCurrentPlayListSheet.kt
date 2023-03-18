package ui.play

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.AppConfig
import base.MusicPlayController
import ui.common.onClick
import ui.common.theme.AppColorsProvider

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CpnCurrentPlayListSheet() {


    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 300),
        skipHalfExpanded = true,
    )

    LaunchedEffect(MusicPlayController.showPlayListSheet) {
        if (MusicPlayController.showPlayListSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }


        ModalBottomSheetLayout(
            sheetContent = {
                Column {
                    Spacer(modifier = Modifier.fillMaxWidth().height(AppConfig.topBarHeight).onClick {
                        MusicPlayController.showPlayListSheet = false
                    })
                    CpnCurrentPlayList {
                        MusicPlayController.showPlayListSheet = false
                    }
                }
            },
            sheetState = sheetState,
            sheetElevation = 0.dp,
            sheetContentColor = Color.Transparent,
            sheetBackgroundColor = Color.Transparent,
            scrimColor = Color.Transparent
        ) {

        }
    }


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CpnCurrentPlayList(hideCallback: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f).fillMaxHeight().onClick {
            hideCallback.invoke()
        })
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(listOf(Color(0x00FFFFFF), AppColorsProvider.current.background))
                )
        )
        LazyColumn(
            modifier = Modifier.width(420.dp).fillMaxHeight().background(AppColorsProvider.current.pure)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier.background(AppColorsProvider.current.pure).padding(vertical = 15.dp, horizontal = 20.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "播放列表",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColorsProvider.current.firstText
                    )
                    Text(
                        "  (共${MusicPlayController.realSongList.size}首)",
                        fontSize = 14.sp,
                        color = AppColorsProvider.current.secondText,
                    )
                }
                Divider(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = AppColorsProvider.current.divider
                )
            }
            items(MusicPlayController.realSongList.size) {
                SongItem(it)
            }
        }
    }
}

@Composable
private fun SongItem(index: Int) {
    val curSongBean = MusicPlayController.realSongList[index]

    Row(
        modifier = Modifier
            .onClick {
                MusicPlayController.playByOriginIndex(index)
            }
            .background(
                if (index % 2 == 0) Color.Transparent else AppColorsProvider.current.divider.copy(
                    0.25f
                )
            ).height(36.dp).fillMaxWidth().padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(2f).fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
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
            modifier = Modifier.padding(start = 10.dp).weight(1f),
            text = curSongBean.ar.getOrNull(0)?.name ?: "未知歌手",
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