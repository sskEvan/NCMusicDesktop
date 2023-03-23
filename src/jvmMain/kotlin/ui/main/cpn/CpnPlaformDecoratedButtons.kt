package ui.main.cpn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
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
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import base.AppConfig
import ui.common.onClick
import util.EnvUtil
import kotlin.system.exitProcess

/**
 * 自定义windows平台关闭、最小化、全屏按钮
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ApplicationScope.CpnWindowsPlaformDecoratedButtons(windowState: WindowState) {

//    LaunchedEffect(windowState.placement) {
//        if (windowState.placement == WindowPlacement.Maximized) {
//            windowState.placement = WindowPlacement.Fullscreen
//            println("Fullscreen done...")
//        }
//    }

    if (EnvUtil.isWindows()) {
        var focusState by remember { mutableStateOf(false) }
        Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            .onPointerEvent(PointerEventType.Enter) {
                focusState = true
            }.onPointerEvent(PointerEventType.Exit) {
                focusState = false
            }) {
            ActionButton("image/ic_window_close.webp", Color(0xFFFC615D), focusState) {
                ::exitApplication
                exitProcess(0)
            }

            ActionButton("image/ic_window_min.webp", Color(0xFFFDBC40), focusState) {
                windowState.isMinimized = true
            }
            val imgUrl = if (AppConfig.fullScreen) {
                "image/ic_window_floating.webp"
            } else {
                "image/ic_window_fullscreen.webp"
            }

            ActionButton(imgUrl, Color(0xFF35CA4A), focusState) {
                if (AppConfig.fullScreen) {
                    windowState.placement = WindowPlacement.Floating
                } else {
                    windowState.placement = WindowPlacement.Maximized
                }
                AppConfig.fullScreen = !AppConfig.fullScreen
            }
        }
    }
}

@Composable
private fun ActionButton(img: String, backgroundColor: Color, focusState: Boolean, onClick: () -> Unit) {

    Box(
        modifier = Modifier.padding(end = 8.dp).clip(CircleShape)
            .size(13.dp).background(backgroundColor).onClick {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {

        if (focusState) {
            Icon(painterResource(img), modifier = Modifier.size(10.dp), contentDescription = null)
        }
    }
}