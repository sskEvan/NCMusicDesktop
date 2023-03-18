package ui.play

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.MusicPlayController
import ui.common.theme.AppColorsProvider

@Composable
fun ColumnScope.CpnSongInfo() {
    MusicPlayController.curSongBean?.let { songBean ->
        Text(songBean.name, color = AppColorsProvider.current.firstText, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 30.dp))
        Row(modifier = Modifier.padding(top = 16.dp)) {
            InfoItem("专辑：", songBean.al.name)
            InfoItem("歌手：", songBean.ar.getOrNull(0)?.name ?: "未知歌手")
        }
    }
}

@Composable
fun RowScope.InfoItem(key: String, value: String) {
    Row(modifier = Modifier.weight(1f)) {
        Text(key, color = AppColorsProvider.current.secondText, fontSize = 12.sp)
        Text(value, color = Color(0xFF5C8DD6), fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}