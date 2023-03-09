package router

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import ui.discovery.DiscoveryPage
import ui.discovery.cpn.CpnRecommendPlayList
import ui.todo.TodoPage

@Composable
fun NavGraph() {
    val navigator = rememberNavigator()

    NavHost(
        navigator = navigator,
        navTransition = remember {
            NavTransition(
                createTransition = fadeIn(),
                destroyTransition = fadeOut(),
                pauseTransition = fadeOut(),
                resumeTransition = fadeIn(),
            )
        }, initialRoute = RouterUrls.DISCOVERY
    ) {
        scene(RouterUrls.DISCOVERY) {
            DiscoveryPage()
        }
        scene(RouterUrls.PODCAST) {
            TodoPage("播客")
        }
        scene(RouterUrls.PERSONAL_FM) {
            TodoPage("私人fm")
        }
        scene(RouterUrls.VIDEO) {
            TodoPage("视频")
        }
        scene(RouterUrls.FOLLOW) {
            TodoPage("关注")
        }
        scene(RouterUrls.FAVORITE_MUSIC) {
            TodoPage("我喜欢的音乐")
        }
        scene(RouterUrls.DOWNLOAD_MANAGER) {
            TodoPage("个性推荐")
        }
        scene(RouterUrls.RECENT_PLAYLIST) {
            TodoPage("最近播放")
        }
        scene(RouterUrls.MY_CLOUD_DISK) {
            TodoPage("我的音乐云盘")
        }
        scene(RouterUrls.MY_PODCAST) {
            TodoPage("我的播客")
        }
        scene(RouterUrls.MY_COLLECT) {
            TodoPage("我的收藏")
        }
        scene(RouterUrls.MY_CREATE_SONG_SHEET) {
            TodoPage("创建的歌单")
        }
        scene(RouterUrls.MY_COLLECT_SONG_SHEET) {
            TodoPage("收藏的歌单")
        }
    }

}