package ui.main.cpn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import base.AppConfig
import moe.tlaster.precompose.ui.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
import ui.play.CpnMusicPlay
import ui.play.CpnSongWords

@Composable
fun CpnMainMusicPlayContainer() {
    val viewModel = viewModel { CpnMainMusicPlayContainerViewModel() }
    println("--------CpnMainMusicPlayContainer show = ${viewModel.show}")
    AnimatedVisibility(
        viewModel.show,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        LazyColumn {
            item {
                Header()
            }
            Body()
        }
    }


}

@Composable
private fun Header() {
    val height = remember { AppConfig.windowMinHeight - AppConfig.topBarHeight - 140.dp }
    Row(modifier = Modifier.fillMaxWidth().height(height)) {
        CpnMusicPlay(Modifier.weight(1f).fillMaxHeight())
        CpnSongWords(Modifier.weight(1f).fillMaxHeight())
    }
}

private fun LazyListScope.Body() {
    items(50) {
        Box(
            modifier = Modifier.height(50.dp).fillMaxWidth().background(Color.Yellow),
            contentAlignment = Alignment.Center
        ) {
            Text("评论-$it")
        }
    }
}

class CpnMainMusicPlayContainerViewModel : ViewModel() {
    var show by mutableStateOf(false)
}