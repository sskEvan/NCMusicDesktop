package ui.main.cpn

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.AppConfig
import base.MusicPlayController
import base.UserManager
import com.google.gson.Gson
import http.NCRetrofitClient
import kotlinx.coroutines.launch
import model.PlaylistDetail
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.ui.viewModel
import router.NCNavigatorManager
import router.RouterUrls
import ui.common.AsyncImage
import ui.common.theme.AppColorsProvider
import ui.login.QrcodeLoginDialog
import base.BaseViewModel
import ui.common.onClick

/**
 * 主页左边菜单栏组件
 */
@Composable
fun CpnMainLeftMenu() {
    val navigator = NCNavigatorManager.navigator
    val loginResult = UserManager.getLoginResultFlow().collectAsState(null).value
    val viewModel: MainLeftMenuViewModel = viewModel { MainLeftMenuViewModel() }
    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            viewModel.getUserPlayList(loginResult.account.id)
        }
    }

    Column(modifier = Modifier.width(200.dp).fillMaxHeight().background(AppColorsProvider.current.background)) {
        Spacer(
            modifier = Modifier.fillMaxWidth().height(AppConfig.topBarHeight)
                .background(if (MusicPlayController.showMusicPlayDrawer) AppColorsProvider.current.pure else AppColorsProvider.current.topBarColor)
        )
        CpnUserInfo()
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CpnMenuItem(viewModel, "image/ic_my_music.webp", "发现音乐") {
                while (navigator.canGoBack) {
                    navigator.popBackStack()
                }
                navigator.navigate(RouterUrls.DISCOVERY, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem(viewModel, "image/ic_podcast.webp", "播客") {
                navigator.navigate(RouterUrls.PODCAST, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem(viewModel, "image/ic_fm.webp", "私人FM") {
                navigator.navigate(RouterUrls.PERSONAL_FM, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem(viewModel, "image/ic_video.webp", "视频") {
                navigator.navigate(RouterUrls.VIDEO, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem(viewModel, "image/ic_follows.webp", "关注") {
                navigator.navigate(RouterUrls.FOLLOW, NavOptions(launchSingleTop = true))
            }
            CpnMyMusicTitle()
            viewModel.favoritePlayList?.let {
                CpnSongSheetItem(viewModel, "image/ic_like.webp", it)
            }
            CpnMenuItem(viewModel, "image/ic_download.webp", "下载管理") {
                navigator.navigate(RouterUrls.DOWNLOAD_MANAGER, NavOptions(launchSingleTop = true))

            }
            CpnMenuItem(viewModel, "image/ic_recent_play_list.webp", "最近播放") {
                navigator.navigate(RouterUrls.RECENT_PLAYLIST, NavOptions(launchSingleTop = true))

            }
            CpnMenuItem(viewModel, "image/ic_cloud.webp", "我的音乐云盘") {
                navigator.navigate(RouterUrls.MY_CLOUD_DISK, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem(viewModel, "image/ic_podcast.webp", "我的播客") {
                navigator.navigate(RouterUrls.MY_PODCAST, NavOptions(launchSingleTop = true))
            }
            CpnMenuItem(viewModel, "image/ic_collect.webp", "我的收藏") {
                navigator.navigate(RouterUrls.MY_COLLECT, NavOptions(launchSingleTop = true))

            }
            viewModel.selfCreatePlayList?.let {
                CpnSongSheet("创建的歌单", it)
            }
            viewModel.collectPlayList?.let {
                CpnSongSheet("收藏的歌单", it)
            }

            LogoutButton()
        }
    }

}

@Composable
private fun CpnUserInfo() {
    val showLoginDialog = rememberSaveable { mutableStateOf(false) }
    val loginResult = UserManager.getLoginResultFlow().collectAsState(null).value

    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).onClick {
            if (loginResult == null) {
                showLoginDialog.value = true
            }
        }.padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            modifier = Modifier.clip(RoundedCornerShape(50)).size(36.dp),
            loginResult?.profile?.avatarUrl ?: "image/ic_default_avator.webp",
            "image/ic_default_avator.webp",
            "image/ic_default_avator.webp"
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
private fun CpnSongSheetItem(viewModel: MainLeftMenuViewModel, icon: String, playlistDetail: PlaylistDetail) {
    CpnMenuItem(viewModel, icon, playlistDetail.name, type = 1) {
        val url = "${RouterUrls.PLAY_LIST_DETAIL}?simplePlayListInfo=${Gson().toJson(playlistDetail.convertToSimple())}"
        println("navigate to PLAY_LIST_DETAIL, url=$url")
        NCNavigatorManager.navigator.navigate(url)
    }
}

/**
 * type:菜单类型，0表普通，1表歌单
 */
@Composable
private fun CpnMenuItem(
    viewModel: MainLeftMenuViewModel,
    logoPath: String,
    title: String,
    markLogoPath: String? = null,
    type: Int = 0,
    onClick: (title: Any) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).onClick {
            if (type == 0) {
                viewModel.selectedMenuTag = title
                viewModel.selectedSongSheetTag = null
            } else {
                viewModel.selectedMenuTag = null
                viewModel.selectedSongSheetTag = title
            }
            onClick(title)
        }.let {
            if (viewModel.selectedMenuTag == title || viewModel.selectedSongSheetTag == title) it.background(
                AppColorsProvider.current.pure
            ) else {
                it
            }
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
    val viewModel: MainLeftMenuViewModel = viewModel { MainLeftMenuViewModel() }
    Row(
        modifier = Modifier.onClick {
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
            CpnSongSheetItem(viewModel, "image/ic_song_sheet.webp", list[i])
        }

    }
}

@Composable
private fun LogoutButton() {
    val loginResult = UserManager.getLoginResultFlow().collectAsState(null).value
    if (loginResult != null) {
        val scope = rememberCoroutineScope()
        val viewModel = viewModel { MainLeftMenuViewModel() }
        Button(modifier = Modifier.padding(top = 60.dp, bottom = 24.dp).width(160.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = AppColorsProvider.current.primary),
            onClick = {
                scope.launch {
                    UserManager.saveLoginResult("")
                    viewModel.favoritePlayList = null
                    viewModel.selfCreatePlayList = null
                    viewModel.collectPlayList = null
                    if (viewModel.selectedSongSheetTag != null) {
                        viewModel.selectedSongSheetTag = null
                        viewModel.selectedMenuTag = "发现音乐"
                        while (NCNavigatorManager.navigator.canGoBack) {
                            NCNavigatorManager.navigator.popBackStack()
                        }
                        NCNavigatorManager.navigator.navigate(RouterUrls.DISCOVERY, NavOptions(launchSingleTop = true))
                    }
                }
            }) {
            Text("退出登陆", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

class MainLeftMenuViewModel : BaseViewModel() {
    var favoritePlayList: PlaylistDetail? by mutableStateOf(null)
    var selfCreatePlayList: List<PlaylistDetail>? by mutableStateOf(null)
    var collectPlayList: List<PlaylistDetail>? by mutableStateOf(null)
    var selectedMenuTag: String? by mutableStateOf("发现音乐")
    var selectedSongSheetTag: String? by mutableStateOf(null)

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