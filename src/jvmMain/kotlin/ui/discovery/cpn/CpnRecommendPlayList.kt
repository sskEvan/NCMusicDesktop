package ui.discovery.cpn

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import http.NCRetrofitClient
import moe.tlaster.precompose.ui.viewModel
import ui.common.ViewStateLazyGridPagingComponent
import base.BaseViewModel

/**
 *  推荐歌单-更多列表组件
 */
@Composable
fun CpnRecommendPlayList() {
    val playListTabSelectedBarViewModel = viewModel { PlayListTabSelectedBarViewModel() }
    val cpnRecommendPlayListViewModel = viewModel { RecommendPlayListViewModel() }

    val requestTag = playListTabSelectedBarViewModel.selectedTab?.name ?: "全部歌单"

    ViewStateLazyGridPagingComponent(modifier = Modifier.fillMaxSize(),
        key = "CpnRecommendPlayList-${requestTag}",
        columns = 4,
        pageSize = 40,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        viewStateContentAlignment = BiasAlignment(0f, -0.6f),
        loadDataBlock = { pageSize, cupage ->
            cpnRecommendPlayListViewModel.getPlayList(requestTag, pageSize, cupage)
        },
        scrollHeader = {
            CpnHighQualityPlayListEntrance(requestTag)
        },
        stickyHeader = {
            CpnPlayListTabSelectedBar()
        }
    ) { data ->
        items(data.playlists.size) {
            CpnPlayListItem(data.playlists[it])
        }
    }

}




class RecommendPlayListViewModel : BaseViewModel() {

    fun getPlayList(tag: String, pageSize: Int, curPage: Int) = launchFlow {
        val offset = (curPage - 1) * pageSize
        println("CpnRecommendPlayListViewModel getPlayList tag=$tag,pageSize=$pageSize,offset=$offset")
        NCRetrofitClient.getNCApi().getPlayList(pageSize, tag, offset)
    }

}


