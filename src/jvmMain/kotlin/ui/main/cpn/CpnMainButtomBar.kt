package ui.main.cpn

import ui.common.SeekBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.MusicPlayController
import ui.common.AsyncImage
import ui.common.theme.AppColorsProvider

@Composable
fun CpnMainButtomBar() {
    Column(modifier = Modifier.background(color = AppColorsProvider.current.pure).fillMaxWidth().height(80.dp)) {
        CpnSeekBar()
        Row(
            modifier = Modifier.padding(horizontal = 20.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CpnMusicInfo()
            CpnMiddleActionButtons()
            CpnRightActionButtons()
        }
    }
}

@Composable
private fun CpnSeekBar() {
    SeekBar(
        progress = 30,
        seeking = {

        },
        seekTo = {

        },
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    )
}

@Composable
private fun RowScope.CpnMusicInfo() {
    Row(
        modifier = Modifier.weight(1f).clickable {
            MusicPlayController.showMusicPlayDrawer = !MusicPlayController.showMusicPlayDrawer
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        MusicPlayController.curSongBean?.let { curSong ->
            AsyncImage(
                Modifier.padding(end = 10.dp).size(48.dp).clip(RoundedCornerShape(4.dp)),
                url = curSong.al.picUrl
            )

            Column {
                Row {
                    Text(curSong.name, fontSize = 14.sp, color = AppColorsProvider.current.firstText)
                    Text(
                        " - ${curSong.ar.getOrNull(0)?.name ?: "未知歌手"}",
                        fontSize = 14.sp,
                        color = AppColorsProvider.current.secondText
                    )
                }
                Text(
                    "00:00 / ${curSong.getSongTimeLength()}",
                    fontSize = 12.sp,
                    color = AppColorsProvider.current.secondText,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }

}

@Composable
private fun RowScope.CpnMiddleActionButtons() {
    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource("image/ic_like.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 30.dp).size(20.dp).padding(2.dp),
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_action_pre.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 20.dp).size(20.dp).padding(2.dp), tint = AppColorsProvider.current.primary
        )
        Icon(
            painterResource("image/ic_action_play.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 20.dp).size(40.dp).padding(2.dp), tint = AppColorsProvider.current.primary
        )
        Icon(
            painterResource("image/ic_action_next.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 30.dp).size(20.dp).padding(2.dp), tint = AppColorsProvider.current.primary
        )
        Icon(
            painterResource("image/ic_share.webp"), contentDescription = null,
            modifier = Modifier.size(20.dp).padding(2.dp),
            tint = AppColorsProvider.current.firstIcon
        )
    }
}

@Composable
private fun RowScope.CpnRightActionButtons() {
    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
        Icon(
            painterResource("image/ic_sound_effect.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(20.dp).padding(2.dp),
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_play_mode_loop.webp"),
            contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(20.dp).padding(2.dp),
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_play_list.webp"),
            contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(20.dp).padding(2.dp),
            tint = AppColorsProvider.current.firstIcon
        )
        Icon(
            painterResource("image/ic_song_words.webp"), contentDescription = null,
            modifier = Modifier.padding(end = 14.dp).size(20.dp).padding(2.dp), tint = AppColorsProvider.current.primary
        )
        Icon(
            painterResource("image/ic_volumn.webp"), contentDescription = null,
            modifier = Modifier.size(20.dp).padding(2.dp),
            tint = AppColorsProvider.current.firstIcon
        )
    }
}