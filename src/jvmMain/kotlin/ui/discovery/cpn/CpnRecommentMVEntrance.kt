package ui.discovery.cpn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import http.NCRetrofitClient
import model.RecommendMVItem
import model.RecommendMVResult
import moe.tlaster.precompose.ui.viewModel
import ui.common.*
import ui.common.theme.AppColorsProvider
import util.StringUtil
import viewmodel.BaseViewModel
import viewmodel.ViewState
import viewmodel.ViewStateMutableStateFlow

/**
 * 推荐MV入口
 */
fun LazyListScope.CpnRecommendMVEntrance(viewModel: CpnRecommendMVEntranceViewModel,
                                         viewState: ViewState<RecommendMVResult>?) {
    item {
        CpnActionMore("推荐MV")
    }

    handleListContent(viewState, reloadDataBlock = {
        viewModel.getRecommendMV(false)
    }) { data ->
        ListToGridItems(data.result, 4) { _, item ->
            RecommendMVItem(item)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RecommendMVItem(item: RecommendMVItem) {
    var focusState by remember { mutableStateOf(false) }

    Column(modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
        focusState = true
    }.onPointerEvent(PointerEventType.Exit) {
        focusState = false
    }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.width(180.dp).height(100.dp).clip(RoundedCornerShape(6.dp)),
                item.picUrl
            )

            Row(
                modifier = Modifier.padding(top = 6.dp, end = 6.dp).align(Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource("image/ic_play_count.webp"),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 6.dp).size(12.dp)
                )
                Text(
                    StringUtil.friendlyNumber(item.playCount),
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
            Column {
                AnimatedVisibility(
                    visible = focusState
                ) {
                    Box(
                        modifier = Modifier.width(180.dp).background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(0.75f), Color.Black.copy(0.25f)
                                    )
                                ), shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                            ).padding(vertical = 6.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            item.copywriter,
                            color = Color.White,
                            fontSize = 12.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Text(
            item.name,
            color = AppColorsProvider.current.firstText,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier.padding(top = 6.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            item.artistName,
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

class CpnRecommendMVEntranceViewModel : BaseViewModel() {

    var flow by mutableStateOf<ViewStateMutableStateFlow<RecommendMVResult>?>(null)
    fun getRecommendMV(firstLoad: Boolean)  {
        if (!firstLoad || flow == null) {
            flow = launchFlow {
                println("获取推荐MV...")
                NCRetrofitClient.getNCApi().getRecommendMV()
            }
        }
    }

}