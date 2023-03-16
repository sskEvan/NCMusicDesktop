package ui.playlist.cpn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Subscribers
import ui.common.AsyncImage
import ui.common.ListToGridItems
import ui.common.NoSuccessComponent
import ui.common.theme.AppColorsProvider
import util.StringUtil

fun LazyListScope.CpnPlayListSubscribers(subscribes: List<Subscribers>?) {  //subscribes: List<Subscribers>

    if (subscribes.isNullOrEmpty()) {
        item {
            NoSuccessComponent()
        }
    } else {
        ListToGridItems(subscribes, 2) { _, item ->
            SubscribersItem(item)
        }
    }

}

@Composable
private fun SubscribersItem(item: Subscribers) {
    Row(
        modifier = Modifier.padding(start = 20.dp).fillMaxWidth().height(120.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.padding(end = 10.dp).size(90.dp).clip(RoundedCornerShape(50)),
            item.avatarUrl,
            "image/ic_default_avator.webp",
            "image/ic_default_avator.webp"
        )
        Column {
            Text(
                item.nickname, color = AppColorsProvider.current.firstText, fontSize = 14.sp, maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (!StringUtil.isEmpty(item.description)) {
                Text(
                    item.description!!, color = AppColorsProvider.current.firstText, fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 10.dp, end = 10.dp)
                )
            }
        }
    }
}