package ui.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CpnSongWords(modifier : Modifier) {
    LazyColumn(modifier) {
        items(50) {
            Box(modifier = Modifier.height(50.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Text("歌词-$it")
            }
        }

    }
}