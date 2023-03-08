package ui.discovery.cpn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.load_the_image.rememberImagePainter
import http.NCRetrofitClient
import model.RecommendPlayListItem
import moe.tlaster.precompose.ui.viewModel
import ui.common.CpnActionMore
import ui.common.ViewStateComponent
import ui.common.theme.AppColorsProvider
import util.ListSplitUtils
import viewmodel.BaseViewModel

@Composable
fun CpnRecommandPlayList(onClickMore: () -> Unit) {
    val viewModel = viewModel { CpnRecommendPlayListViewModel() }

    Column {
        CpnActionMore("推荐歌单") {
            onClickMore.invoke()
        }

        ViewStateComponent(
            viewStateComponentModifier = Modifier.fillMaxWidth().height(400.dp),
            loadDataBlock = { viewModel.getRecommendPlayList() }) { data ->
            Content(data.result)
        }
    }
}

@Composable
private fun Content(list: List<RecommendPlayListItem>) {
    val groupList = remember {
        ListSplitUtils.splitList(list, 5)
    }
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        groupList.forEach {
            RecommendPlayListItemRow(it)
        }
    }
}

@Composable
private fun RecommendPlayListItemRow(list: List<RecommendPlayListItem>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        list.forEach {
            RecommendPlayListItem(it)
        }
    }

}

@Composable
private fun RowScope.RecommendPlayListItem(item: RecommendPlayListItem) {
    Column(
        modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            rememberImagePainter(item.picUrl),
            contentDescription = "",
            modifier = Modifier.size(136.dp).clip(RoundedCornerShape(6.dp))
        )
        Text(
            item.name,
            color = AppColorsProvider.current.firstText,
            fontSize = 12.sp,
            maxLines = 2,
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp).height(48.dp)
        )
    }

}

class CpnRecommendPlayListViewModel : BaseViewModel() {

    fun getRecommendPlayList() = launch {
        println("获取推荐歌单...")
        NCRetrofitClient.getNCApi().getRecommendPlayList(25)
    }

}