package ui.play

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import moe.tlaster.precompose.ui.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
import ui.common.AsyncImage
import ui.common.theme.AppColorsProvider

const val DISK_ROTATE_ANIM_CYCLE = 10000

/**
 * 音乐播放组件
 */
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
    val viewModel: MusicPlayViewModel = viewModel { MusicPlayViewModel() }

    LaunchedEffect(MusicPlayController.curRealIndex) {
        viewModel.lastSheetDiskRotateAngleForSnap = 0f
        viewModel.sheetDiskRotate.snapTo(viewModel.lastSheetDiskRotateAngleForSnap)
    }

    LaunchedEffect(MusicPlayController.isPlaying()) {
        if (MusicPlayController.isPlaying()) {
            viewModel.sheetDiskRotate.animateTo(
                targetValue = 360f + viewModel.lastSheetDiskRotateAngleForSnap,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = DISK_ROTATE_ANIM_CYCLE, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                viewModel.lastSheetDiskRotateAngleForSnap =  viewModel.sheetDiskRotate.value
            }
        } else {
            viewModel.sheetDiskRotate.snapTo(viewModel.lastSheetDiskRotateAngleForSnap)
            viewModel.sheetDiskRotate.stop()
        }

    }

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
                )
                .graphicsLayer {
                    rotationZ = viewModel.sheetDiskRotate.value
                },
            url = it.al.picUrl
        )
    }
}

@Composable
private fun DiskNeedle() {

    val needleRotateAnim by animateFloatAsState(
        targetValue = if (!MusicPlayController.isPlaying()) -25f else 0f,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
    )

    Image(
        painter = painterResource("image/ic_play_neddle.webp"),
        modifier = Modifier
            .padding(start = 72.dp)
            .width(114.dp)
            .height(174.dp)
            .graphicsLayer(
                rotationZ = needleRotateAnim,
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

class MusicPlayViewModel : ViewModel() {

    // disk旋转动画
    val sheetDiskRotate by mutableStateOf(Animatable(0f))

    // 上一次disk旋转角度
    var lastSheetDiskRotateAngleForSnap = 0f

    // 是否抬起磁针
    var sheetNeedleUp by mutableStateOf(true)

}