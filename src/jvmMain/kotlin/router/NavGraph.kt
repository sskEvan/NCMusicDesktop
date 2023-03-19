package router

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.gson.Gson
import model.SimplePlayListItem
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.query
import moe.tlaster.precompose.navigation.transition.NavTransition
import ui.discovery.DiscoveryPage
import ui.playlist.PlayListDetailPage
import ui.todo.TodoPage


object NCNavigatorManager {
    lateinit var navigator: Navigator
}

@Composable
fun NavGraph() {
    val navigator = NCNavigatorManager.navigator
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

        scene("${RouterUrls.PLAY_LIST_DETAIL}") {backStackEntry ->
            val simplePlayListInfo = backStackEntry.query<String>("simplePlayListInfo")
            val simplePlayListItem = Gson().fromJson(simplePlayListInfo, SimplePlayListItem::class.java)
            PlayListDetailPage(simplePlayListItem)
        }
    }

}