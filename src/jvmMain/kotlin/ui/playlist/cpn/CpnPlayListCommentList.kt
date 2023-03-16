package ui.playlist.cpn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.CommentBean
import model.CommentResult
import http.NCRetrofitClient
import ui.common.AsyncImage
import ui.common.PaingFooterNumBar
import ui.common.handleListContent
import ui.common.theme.AppColorsProvider
import util.TimeUtil
import viewmodel.BaseViewModel
import viewmodel.ViewState
import viewmodel.ViewStateMutableStateFlow


fun LazyListScope.CpnPlayListCommentList(
    viewState: ViewState<CommentResult>?,
    viewModel: CpnPlayListCommentListViewModel,
    reloadCallback: (curPage: Int) -> Unit
) {
    handleListContent(viewState,
        reloadDataBlock = {
            reloadCallback.invoke(viewModel.cutPage)
        }) { data ->
        items(data.comments.size) {
            CommentItem(data.comments[it])
        }

        // 底部分页组件
        if (data.total > CpnPlayListCommentListViewModel.pageSize) {
            item {
                PaingFooterNumBar(data.total, CpnPlayListCommentListViewModel.pageSize, viewModel.cutPage) {
                    reloadCallback.invoke(it)
                }
            }
        }
    }
}

@Composable
private fun CommentItem(commentBean: CommentBean) {
    Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        AsyncImage(
            modifier = Modifier.padding(end = 10.dp).size(48.dp).clip(RoundedCornerShape(50)),
            url = commentBean.user.avatarUrl,
            "image/ic_default_avator.webp"
        )
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF5C8DD6), fontSize = 14.sp)) {
                        append(commentBean.user.nickname)
                    }
                    withStyle(style = SpanStyle(color = AppColorsProvider.current.firstText, fontSize = 14.sp)) {
                        append(" : ${commentBean.content}")
                    }
                },
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = TimeUtil.parse(commentBean.time),
                    color = AppColorsProvider.current.thirdText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp).weight(1f)
                )

                Icon(
                    painter = painterResource("image/ic_thumbs_up.webp"),
                    modifier = Modifier.size(14.dp),
                    contentDescription = "",
                    tint = AppColorsProvider.current.secondText
                )

                Divider(
                    modifier = Modifier.padding(horizontal = 14.dp).width(0.5.dp),
                    color = AppColorsProvider.current.divider,
                    thickness = 10.dp
                )

                Icon(
                    painter = painterResource("image/ic_share.webp"),
                    modifier = Modifier.size(14.dp),
                    contentDescription = "",
                    tint = AppColorsProvider.current.secondIcon
                )

                Divider(
                    modifier = Modifier.padding(horizontal = 14.dp).width(0.5.dp),
                    color = AppColorsProvider.current.divider,
                    thickness = 10.dp
                )

                Icon(
                    painter = painterResource("image/ic_comment.webp"),
                    modifier = Modifier.size(14.dp),
                    contentDescription = "",
                    tint = AppColorsProvider.current.secondIcon
                )

            }


            Divider(modifier = Modifier.padding(top = 8.dp).fillMaxWidth(), color = AppColorsProvider.current.divider, thickness = 0.5.dp)
        }

    }
}

class CpnPlayListCommentListViewModel : BaseViewModel() {
    companion object {
        const val pageSize = 20
    }

    var cutPage by mutableStateOf(1)
    var flow by mutableStateOf<ViewStateMutableStateFlow<CommentResult>?>(null)

    fun initFetchData(id: Long) {
        if (flow == null) {
            cutPage = 1
            flow = launchFlow {
                NCRetrofitClient.getNCApi().getPlayListComment(id, pageSize, 0)
            }
        }
    }

    fun fetchDataPaging(id: Long, curPage: Int, firstLoad: Boolean = false) {
        if (!firstLoad || flow == null) {
            cutPage = curPage
            val offset = (curPage - 1) * pageSize
            flow = launchFlow {
                NCRetrofitClient.getNCApi().getPlayListComment(id, pageSize, offset)
            }
        }
    }
}