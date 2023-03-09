package ui.main.cpn

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.rememberNavigator
import router.RouterUrls
import ui.common.theme.AppColorsProvider
import ui.login.QrcodeLoginDialog

@Composable
fun CpnMainLeftMenu() {
    val navigator = rememberNavigator()
    Column(modifier = Modifier.width(200.dp).background(AppColorsProvider.current.background)) {
        Spacer(modifier = Modifier.fillMaxWidth().height(50.dp).background(AppColorsProvider.current.topBarColor))
        CpnUserInfo()
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            CpndMenuItem("image/ic_my_music.webp", "发现音乐") {
                navigator.navigate(RouterUrls.DISCOVERY, NavOptions(launchSingleTop = true))
            }
            CpndMenuItem("image/ic_podcast.webp", "播客") {
                navigator.navigate(RouterUrls.PODCAST)
            }
            CpndMenuItem("image/ic_fm.webp", "私人FM") {
                navigator.navigate(RouterUrls.PERSONAL_FM)
            }
            CpndMenuItem("image/ic_video.webp", "视频") {
                navigator.navigate(RouterUrls.VIDEO)
            }
            CpndMenuItem("image/ic_follows.webp", "关注") {
                navigator.navigate(RouterUrls.FOLLOW)
            }
            CpnMyMusicTitle()
            CpndMenuItem("image/ic_like.webp", "我喜欢的音乐") {
                navigator.navigate(RouterUrls.FAVORITE_MUSIC)
            }
            CpndMenuItem("image/ic_download.webp", "下载管理") {
                navigator.navigate(RouterUrls.DOWNLOAD_MANAGER)

            }
            CpndMenuItem("image/ic_recent_play_list.webp", "最近播放") {
                navigator.navigate(RouterUrls.RECENT_PLAYLIST)

            }
            CpndMenuItem("image/ic_cloud.webp", "我的音乐云盘") {
                navigator.navigate(RouterUrls.MY_CLOUD_DISK)

            }
            CpndMenuItem("image/ic_podcast.webp", "我的播客") {
                navigator.navigate(RouterUrls.MY_PODCAST)

            }
            CpndMenuItem("image/ic_collect.webp", "我的收藏") {
                navigator.navigate(RouterUrls.MY_COLLECT)

            }
            CpnSongSheet("创建的歌单", 8) {
                navigator.navigate(RouterUrls.MY_CREATE_SONG_SHEET)

            }
            CpnSongSheet("收藏的歌单", 15) {
                navigator.navigate(RouterUrls.MY_COLLECT_SONG_SHEET)

            }
        }
    }

}

@Composable
private fun CpnUserInfo() {
    val showLoginDialog = rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).clickable {
            showLoginDialog.value = true
        }.padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource("image/ic_default_avator.webp"),
            contentDescription = "头像",
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = "未登录",
            modifier = Modifier.padding(horizontal = 10.dp),
            fontSize = 14.sp,
            maxLines = 1,
            color = AppColorsProvider.current.firstText,
            overflow = TextOverflow.Ellipsis
        )
        Icon(painterResource("image/ic_triangle_right.webp"), contentDescription = "", modifier = Modifier.size(8.dp))
    }
    QrcodeLoginDialog(showLoginDialog)
}

@Composable
private fun CpndMenuItem(
    logoPath: String, title: String, markLogoPath: String? = null, onClick: (title: String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).clickable {
            onClick(title)
        }.padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(logoPath),
            contentDescription = title,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF000000)
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
            fontSize = 12.sp,
            maxLines = 1,
            color = AppColorsProvider.current.firstText,
            overflow = TextOverflow.Ellipsis
        )
        markLogoPath?.let {
            Icon(painterResource(it), contentDescription = null, modifier = Modifier.size(14.dp))
        }

    }
}

@Composable
private fun CpnMyMusicTitle() {
    Text(
        text = "我的音乐",
        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp).fillMaxWidth().padding(horizontal = 16.dp),
        fontSize = 12.sp,
        maxLines = 1,
        color = AppColorsProvider.current.secondText,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ColumnScope.CpnSongSheet(title: String, count: Int, onItemClick: (title: String) -> Unit) {
    var expanded by remember { mutableStateOf(true) }
    val animValue = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.clickable {
            expanded = !expanded
            println("ssk-ui.main.cpn.CpnSongSheet-$title:expanded=$expanded")
            scope.launch {
                animValue.animateTo(if (expanded) 1f else 0f)
            }
        }.padding(top = 16.dp, bottom = 8.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource("image/ic_triangle_right.webp"),
            contentDescription = "",
            modifier = Modifier.padding(start = 8.dp).size(8.dp).rotate(
                90 * animValue.value
            ),
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
            fontSize = 12.sp,
            maxLines = 1,
            color = AppColorsProvider.current.secondText,
            overflow = TextOverflow.Ellipsis
        )
    }

    Column(modifier = Modifier.fillMaxWidth().height((40f * animValue.value * count).dp)) {
        repeat(count) {
            CpndMenuItem("image/ic_song_sheet.webp", "$title--${it}") {
                onItemClick(it)
            }
        }
    }

}