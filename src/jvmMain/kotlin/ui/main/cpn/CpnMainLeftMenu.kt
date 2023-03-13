package ui.main.cpn

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.UserManager
import com.google.gson.Gson
import com.lt.load_the_image.rememberImagePainter
import http.NCRetrofitClient
import kotlinx.coroutines.launch
import model.PlaylistDetail
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.ui.viewModel
import router.NCNavigatorManager
import router.RouterUrls
import ui.common.theme.AppColorsProvider
import ui.login.QrcodeLoginDialog
import viewmodel.BaseViewModel

@Composable
fun CpnMainLeftMenu() {
//    val navigator = rememberNavigator()
    val navigator = NCNavigatorManager.navigator
    val loginResult = UserManager.getLoginResultFlow().collectAsState(null).value
    val viewModel: CpnMainLeftMenuViewModel = viewModel { CpnMainLeftMenuViewModel() }
    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            viewModel.getUserPlayList(loginResult.account.id)
        }
    }

    Column(modifier = Modifier.width(200.dp).background(AppColorsProvider.current.background)) {
        Spacer(modifier = Modifier.fillMaxWidth().height(50.dp).background(AppColorsProvider.current.topBarColor))
        CpnUserInfo()
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            CpnMenuItem("image/ic_my_music.webp", "发现音乐") {
                navigator.navigate(RouterUrls.DISCOVERY, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem("image/ic_podcast.webp", "播客") {
                navigator.navigate(RouterUrls.PODCAST)
            }
            CpnMenuItem("image/ic_fm.webp", "私人FM") {
                navigator.navigate(RouterUrls.PERSONAL_FM)
            }
            CpnMenuItem("image/ic_video.webp", "视频") {
                navigator.navigate(RouterUrls.VIDEO)
            }
            CpnMenuItem("image/ic_follows.webp", "关注") {
                navigator.navigate(RouterUrls.FOLLOW)
            }
            CpnMyMusicTitle()
            viewModel.favoritePlayList?.let {
                CpnSongSheetItem("image/ic_like.webp", it)
            }
            CpnMenuItem("image/ic_download.webp", "下载管理") {
                navigator.navigate(RouterUrls.DOWNLOAD_MANAGER)

            }
            CpnMenuItem("image/ic_recent_play_list.webp", "最近播放") {
                navigator.navigate(RouterUrls.RECENT_PLAYLIST)

            }
            CpnMenuItem("image/ic_cloud.webp", "我的音乐云盘") {
                navigator.navigate(RouterUrls.MY_CLOUD_DISK)

            }
            CpnMenuItem("image/ic_podcast.webp", "我的播客") {
                navigator.navigate(RouterUrls.MY_PODCAST)

            }
            CpnMenuItem("image/ic_collect.webp", "我的收藏") {
                navigator.navigate(RouterUrls.MY_COLLECT)

            }

            viewModel.selfCreatePlayList?.let {
                CpnSongSheet("创建的歌单", it)
            }
            viewModel.collectPlayList?.let {
                CpnSongSheet("收藏的歌单", it)
            }
        }
    }

}

@Composable
private fun CpnUserInfo() {
    val showLoginDialog = rememberSaveable { mutableStateOf(false) }
    val loginResult = UserManager.getLoginResultFlow().collectAsState(null).value

    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).clickable {
            if (loginResult == null) {
                showLoginDialog.value = true
            }
        }.padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            rememberImagePainter(loginResult?.profile?.avatarUrl ?: "image/ic_default_avator.webp", "image/ic_default_avator.webp"),
            contentDescription = "头像",
            modifier = Modifier.clip(RoundedCornerShape(50)).size(36.dp)
        )


        Text(
            text = loginResult?.profile?.nickname ?: "未登录",
            modifier = Modifier.padding(horizontal = 10.dp),
            fontSize = 14.sp,
            maxLines = 1,
            color = AppColorsProvider.current.firstText,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            painterResource("image/ic_triangle_right.webp"), contentDescription = "", modifier = Modifier.size(8.dp),
            tint = AppColorsProvider.current.firstIcon
        )
    }
    QrcodeLoginDialog(showLoginDialog)
}

@Composable
private fun CpnSongSheetItem(icon: String, playlistDetail: PlaylistDetail) {
    CpnMenuItem(icon, playlistDetail.name) {
        val url = "${RouterUrls.PLAY_LIST_DETAIL}?simplePlayListInfo=${Gson().toJson(playlistDetail.convertToSimple())}"
        println("navigate to PLAY_LIST_DETAIL, url=$url")
        NCNavigatorManager.navigator.navigate(url)
    }
}

@Composable
private fun CpnMenuItem(
    logoPath: String, title: String, markLogoPath: String? = null, onClick: (title: Any) -> Unit
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
            tint = AppColorsProvider.current.firstIcon
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
private fun ColumnScope.CpnSongSheet(title: String, list: List<PlaylistDetail>) {
    var expanded by remember { mutableStateOf(true) }
    val animValue = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.clickable {
            expanded = !expanded
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
            tint = AppColorsProvider.current.firstIcon
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

    Column(modifier = Modifier.fillMaxWidth().height((40f * animValue.value * list.size).dp)) {
        for (i in 0 until list.size) {
            CpnSongSheetItem("image/ic_song_sheet.webp", list[i])
        }

    }

}

class CpnMainLeftMenuViewModel : BaseViewModel() {
    var favoritePlayList: PlaylistDetail? by mutableStateOf(null)
    var selfCreatePlayList: List<PlaylistDetail>? by mutableStateOf(null)
    var collectPlayList: List<PlaylistDetail>? by mutableStateOf(null)

    fun getUserPlayList(userId: Long) {
        launch(handleSuccessBlock = {
            val selfCreateList = mutableListOf<PlaylistDetail>()
            val collectList = mutableListOf<PlaylistDetail>()

            it.playlist.forEach { PlaylistDetail ->
                if (PlaylistDetail.creator.userId == userId) {
                    if (PlaylistDetail.name == PlaylistDetail.creator.nickname + "喜欢的音乐") {
                        favoritePlayList = PlaylistDetail
                    } else {
                        selfCreateList.add(PlaylistDetail)
                    }
                } else {
                    collectList.add(PlaylistDetail)
                }
            }
            selfCreatePlayList = selfCreateList
            collectPlayList = collectList
        }) {
            NCRetrofitClient.getNCApi().getUserPlayList(userId.toString())
        }
    }
}