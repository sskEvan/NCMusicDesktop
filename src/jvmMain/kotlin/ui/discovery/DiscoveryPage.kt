package ui.discovery

import androidx.compose.runtime.*

@Composable
fun DiscoveryPage() {
    val tabs = remember { listOf("个性推荐", "歌单", "排行榜", "歌手", "最新音乐") }
    var selectedIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
//    val pagerState = rememberPagerState(
//        initialPage = 0,
//    )


}