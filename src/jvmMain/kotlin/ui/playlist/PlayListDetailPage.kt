package ui.playlist

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.load_the_image.rememberImagePainter
import model.SimplePlayListItem
import moe.tlaster.precompose.ui.viewModel
import ui.common.CommonTabLayout
import ui.common.CommonTabLayoutStyle
import ui.common.ExpandableText
import ui.common.theme.AppColorsProvider
import ui.main.cpn.CommonTopBar
import ui.playlist.cpn.*
import util.StringUtil
import util.TimeUtil
import viewmodel.BaseViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayListDetailPage(simplePlayListItem: SimplePlayListItem) {
    val lazyListState = rememberLazyListState()
    var showStickyHeader by remember { mutableStateOf(false) }
    val detailPageViewModel = viewModel { PlayListDetailPageViewModel() }
    val trackListViewModel = viewModel { TrackListViewModel() }
    val commentViewModel = viewModel { CpnPlayListCommentListViewModel() }

    val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    LaunchedEffect(Unit) {
        snapshotFlow { firstVisibleItemIndex }
            .collect { firstVisibleItemIndex ->
                showStickyHeader = firstVisibleItemIndex > 0 && detailPageViewModel.selectedTabIndex.value == 0
            }
    }

    // 首次获取数据
    LaunchedEffect(detailPageViewModel.selectedTabIndex.value) {
        when (detailPageViewModel.selectedTabIndex.value) {
            0 -> {  // 歌曲列表
                trackListViewModel.fetchData(simplePlayListItem.id, true)
            }

            1 -> {  // 评论
                commentViewModel.fetchDataPaging(simplePlayListItem.id, 1, true)
            }

        }
    }
    val trackListViewState = trackListViewModel.flow?.collectAsState()
    val commentViewState = commentViewModel.flow?.collectAsState()

    Column {
        CommonTopBar("歌单详情", showBackButton = true)

        Box {
            LazyColumn(state = lazyListState) {
                item {
                    HeadInfo(simplePlayListItem)
                    TabBar()
                }

                when (detailPageViewModel.selectedTabIndex.value) {
                    0 -> {  // 歌曲列表
                        CpnTrackList(trackListViewState?.value) {
                            trackListViewModel.fetchData(simplePlayListItem.id)
                        }
                    }

                    1 -> {  // 评论
                        CpnPlayListCommentList(commentViewState?.value, commentViewModel) {curPage ->
                            println("commentViewModel.fetchDataPaging 2")
                            commentViewModel.fetchDataPaging(simplePlayListItem.id, curPage)
                        }
                    }

                    2 -> {  // 收藏者
                        CpnPlayListSubscribers(trackListViewModel.playlistDetailResult?.playlist?.subscribers)
                    }
                }

            }
            if (showStickyHeader) {
                StickyHeader(simplePlayListItem)
            }
        }
    }
}

@Composable
private fun HeadInfo(simplePlayListItem: SimplePlayListItem) {
    val viewModel = viewModel { TrackListViewModel() }
    var playlistDetailResult = viewModel.playlistDetailResult
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Image(
            rememberImagePainter(simplePlayListItem.picUrl, placeholderResource = "image/ic_disk_place_holder.webp"),
            modifier = Modifier.padding(end = 20.dp).size(216.dp).clip(RoundedCornerShape(6.dp)),
            contentDescription = ""
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.padding(end = 6.dp)
                        .border(BorderStroke(1.dp, color = AppColorsProvider.current.primary), RoundedCornerShape(2.dp))
                ) {
                    Text(
                        "歌单",
                        color = AppColorsProvider.current.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        fontSize = 14.sp
                    )
                }
                Text(
                    simplePlayListItem.name,
                    color = AppColorsProvider.current.firstText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                playlistDetailResult?.playlist?.creator?.let { creator ->
                    Image(
                        rememberImagePainter(creator.avatarUrl),
                        contentDescription = null,
                        modifier = Modifier.clip(RoundedCornerShape(50)).size(20.dp)
                    )
                    Text(
                        creator.nickname,
                        color = AppColorsProvider.current.firstText,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }

                Text(
                    "${TimeUtil.parse(simplePlayListItem.trackNumberUpdateTime)}创建",
                    color = AppColorsProvider.current.secondText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    fontSize = 12.sp
                )
            }

            Row(
                modifier = Modifier.padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeadInfoActionButton(
                    "播放全部",
                    "image/ic_action_play.webp",
                    AppColorsProvider.current.primary,
                    Color.White,
                    Color.White
                )
                HeadInfoActionButton(
                    "收藏(${StringUtil.friendlyNumber(playlistDetailResult?.playlist?.subscribedCount ?: simplePlayListItem.subscribedCount)})",
                    "image/ic_collect.webp"
                )
                HeadInfoActionButton(
                    "分享(${StringUtil.friendlyNumber(playlistDetailResult?.playlist?.shareCount ?: simplePlayListItem.shareCount)})",
                    "image/ic_share.webp"
                )
                HeadInfoActionButton("下载", "image/ic_download.webp")
            }

            val tabs = remember(playlistDetailResult) {
                val sb = StringBuilder()
                sb.append("标签:")
                playlistDetailResult?.playlist?.tags?.forEachIndexed { index, tag ->
                    sb.append(tag)
                    if (index < playlistDetailResult.playlist.tags.size - 1) {
                        sb.append("/")
                    }
                }
                sb.toString()
            }

            Text(
                tabs,
                color = AppColorsProvider.current.firstIcon,
                modifier = Modifier.padding(vertical = 4.dp).padding(end = 20.dp),
                fontSize = 12.sp
            )

            Row(
                modifier = Modifier.padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "歌曲数:${StringUtil.friendlyNumber(simplePlayListItem.trackCount)}",
                    color = AppColorsProvider.current.firstIcon,
                    modifier = Modifier.padding(end = 20.dp),
                    fontSize = 12.sp
                )

                Text(
                    "播放数:${StringUtil.friendlyNumber(simplePlayListItem.playCount)}",
                    color = AppColorsProvider.current.firstIcon,
                    fontSize = 12.sp
                )
            }

            ExpandableText(
                text = "简介:${playlistDetailResult?.playlist?.description ?: simplePlayListItem.copywriter ?: "暂无"}",
                color = AppColorsProvider.current.firstIcon,
                modifier = Modifier.padding(top = 6.dp).padding(end = 20.dp),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun HeadInfoActionButton(
    title: String,
    icon: String,
    bgColor: Color = Color.Transparent,
    textColor: Color = AppColorsProvider.current.firstText,
    iconTint: Color = AppColorsProvider.current.firstIcon,
) {
    Row(
        modifier = Modifier.padding(end = 8.dp)
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon),
            modifier = Modifier.padding(end = 8.dp).size(20.dp).clip(RoundedCornerShape(6.dp)),
            contentDescription = "",
            tint = iconTint
        )

        Text(
            title,
            color = textColor,
            fontSize = 14.sp
        )
    }

}


@Composable
private fun StickyHeader(simplePlayListItem: SimplePlayListItem) {
    Column(
        modifier = Modifier.fillMaxWidth().background(AppColorsProvider.current.pure)
            .padding(start = 30.dp, top = 10.dp, end = 30.dp)
    ) {
        Text(
            simplePlayListItem.name,
            color = AppColorsProvider.current.firstText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            Icon(
                painter = painterResource("image/ic_action_play.webp"),
                modifier = Modifier.padding(end = 30.dp).size(24.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.primary
            )
            Icon(
                painter = painterResource("image/ic_collect.webp"),
                modifier = Modifier.padding(end = 30.dp).size(22.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.firstIcon
            )
            Icon(
                painter = painterResource("image/ic_share.webp"),
                modifier = Modifier.padding(end = 30.dp).size(22.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.firstIcon
            )
            Icon(
                painter = painterResource("image/ic_download.webp"),
                modifier = Modifier.padding(end = 30.dp).size(22.dp),
                contentDescription = "",
                tint = AppColorsProvider.current.firstIcon
            )
        }

        Divider(modifier = Modifier.fillMaxWidth(), color = AppColorsProvider.current.divider)
    }
}


@Composable
private fun TabBar() {
    val datailViewModel = viewModel { PlayListDetailPageViewModel() }
    val trackListViewModel = viewModel { TrackListViewModel() }
    val tabs = remember(trackListViewModel.playlistDetailResult) {
        listOf(
            "歌曲列表",
            "评论(${StringUtil.friendlyNumber(trackListViewModel.playlistDetailResult?.playlist?.commentCount)})",
            "收藏者"
        )
    }
    CommonTabLayout(
        selectedIndex = datailViewModel.selectedTabIndex.value, tabTexts = tabs,
        backgroundColor = Color.Transparent,
        style = CommonTabLayoutStyle(modifier = Modifier.height(40.dp), showIndicator = true)
    ) {
        datailViewModel.selectedTabIndex.value = it
    }
    Divider(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        color = AppColorsProvider.current.divider,
        thickness = 1.dp
    )
}

class PlayListDetailPageViewModel : BaseViewModel() {
    val selectedTabIndex = mutableStateOf(0)
}

