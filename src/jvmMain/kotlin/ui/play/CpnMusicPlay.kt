package ui.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CpnMusicPlay(modifier : Modifier) {
    Box(modifier.background(Color.Blue), contentAlignment = Alignment.Center) {
        Text("播放界面")
    }
}