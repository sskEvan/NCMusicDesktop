package ui.main.cpn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import ui.discovery.DiscoveryPage


@Composable
fun CpnMainRightContainer(title: String) {
    DiscoveryPage()
//    Box(
//        modifier = Modifier.fillMaxSize().background(Color.White),
//        contentAlignment = Alignment.Center
//    ) {
//       Text(title, fontSize = 24.sp)
//    }
}