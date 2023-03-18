package ui.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.MusicPlayController
import ui.common.AsyncImage
import ui.common.theme.AppColorsProvider

@Composable
fun CpnMusicPlay(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.TopCenter) {
            Box(modifier = Modifier.padding(top = 80.dp)) {
                DiskRoundBackground()
                Disk()
            }
            DiskNeedle()
        }
        ActionIconRow()
        Spacer(modifier = Modifier.weight(1f))
        CommentTitle()
    }

}

@Composable
private fun DiskRoundBackground() {
    // 半透明圆形背景
    Box(
        modifier = Modifier
            .width(328.dp)
            .height(328.dp)
            .clip(CircleShape)
            .background(Color(0x55EEEEEE))
    )
}

@Composable
private fun BoxScope.Disk() {
    Image(
        painter = painterResource("image/ic_disk_around.webp"),
        modifier = Modifier
            .width(320.dp)
            .height(320.dp)
            .align(Alignment.Center),
        contentDescription = ""
    )

    MusicPlayController.curSongBean?.let {
        AsyncImage(
            modifier = Modifier
                .width(220.dp)
                .height(220.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = CircleShape
                ), url = it.al.picUrl
        )
    }
}

@Composable
private fun DiskNeedle() {
    Image(
        painter = painterResource("image/ic_play_neddle.webp"),
        modifier = Modifier
            .padding(start = 72.dp)
            .width(114.dp)
            .height(174.dp)
            .graphicsLayer(
                rotationZ = 0f,
                transformOrigin = TransformOrigin(0.164f, 0.109f)
            ),
        contentDescription = ""
    )
}

@Composable
private fun ActionIconRow() {
    Row(modifier = Modifier.padding(top = 20.dp)) {
        ActionIcon("image/ic_like.webp")
        ActionIcon("image/ic_collect.webp")
        ActionIcon("image/ic_download.webp")
        ActionIcon("image/ic_share.webp")

    }
}

@Composable
private fun ActionIcon(icon: String) {
    Box(
        modifier = Modifier.padding(horizontal = 20.dp).background(AppColorsProvider.current.background, CircleShape)
            .size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = AppColorsProvider.current.firstIcon
        )
    }

}

@Composable
private fun CommentTitle() {
    Text(
        text = "听友评论",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = AppColorsProvider.current.firstText,
        modifier = Modifier.padding(start = 50.dp, bottom = 10.dp).fillMaxWidth()
    )
}