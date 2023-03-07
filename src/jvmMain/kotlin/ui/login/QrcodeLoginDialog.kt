package ui.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import com.lt.load_the_image.rememberImagePainter
import util.QrcodeUtil
import kotlinx.coroutines.launch

@Composable
fun QrcodeLoginDialog(show: MutableState<Boolean>) {
    Dialog(
        onCloseRequest = { show.value = false }, visible = show.value,
        state = rememberDialogState(size = DpSize(400.dp, 480.dp)),
        resizable = false,
        title = ""
    ) {
        QrcodeLoginDialogContent()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun QrcodeLoginDialogContent() {
    var focusState by remember { mutableStateOf(false) }
    val totalWidth = remember { 400.dp }
    val smallWidth = remember { 180.dp }
    val largeWidth = remember { 220.dp }

    val imageInitOffset = remember { (totalWidth - largeWidth) / 2 + largeWidth - smallWidth }
    val imageTargetOffset = remember { (totalWidth - smallWidth * 2) / 2 }
    val qrcodeInitOffset = remember { (totalWidth - largeWidth) / 2 }
    val qrcodeTargetOffset = remember { totalWidth - (totalWidth - smallWidth * 2) / 2 - smallWidth }


    val scope = rememberCoroutineScope()
    val offsetAnim = remember { Animatable(0f) }

    Column(
        modifier = Modifier.height(480.dp).background(Color.White).padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            "扫码登录",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Box(modifier = Modifier.width(totalWidth)
            .onPointerEvent(PointerEventType.Enter) {
                focusState = true
                scope.launch {
                    offsetAnim.animateTo(1f, tween(600))
                }
            }
            .onPointerEvent(PointerEventType.Exit) {
                focusState = false
                scope.launch {
                    offsetAnim.animateTo(0f, tween(600))
                }

            },
            contentAlignment = Alignment.CenterStart
        ) {
            CpnScanTipImage(
                Modifier.width(smallWidth).height(260.dp).padding(12.dp)
                    .scale(offsetAnim.value * 0.3f + 0.7f)
                    .offset(imageInitOffset + (imageTargetOffset - imageInitOffset) * offsetAnim.value)
                    .alpha(offsetAnim.value)
            )
            val cpnScanQrcodeWidth = largeWidth - (largeWidth - smallWidth) * offsetAnim.value
            CpnScanQrcode(
                Modifier.width(cpnScanQrcodeWidth)
                    .height(300.dp)
                    .offset(qrcodeInitOffset + (qrcodeTargetOffset - qrcodeInitOffset) * offsetAnim.value)
                    .padding(top = 20.dp),
                cpnScanQrcodeWidth
            )

        }
    }
}

@Composable
private fun CpnScanTipImage(modifier: Modifier) {
    Image(painterResource("image/ic_scan_code_tip.webp"), contentDescription = "扫描二维码提示", modifier = modifier)
}

@Composable
private fun CpnScanQrcode(modifier: Modifier, qrcodeSize: Dp) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            rememberImagePainter(
                QrcodeUtil.createQrcodeFile(
                    "www.baidu.com",
                    500,
                    500
                )!!
            ),
            contentDescription = "",
            modifier = Modifier.size(qrcodeSize)
        )
        Text(
            "请使用网易云音乐APP\n扫码登录",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}