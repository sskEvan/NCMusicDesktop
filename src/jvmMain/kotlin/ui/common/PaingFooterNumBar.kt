package ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.common.theme.AppColorsProvider


/**
 * 分页页数footerBar
 */
@Composable
fun PaingFooterNumBar(totalNum: Int, pageSize: Int, curPage: Int, onSelectedPageCallback: (curPage: Int) -> Unit) {
    val totalPage = remember { (totalNum + (pageSize - 1)) / pageSize }
    Row(
        modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth().height(30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PaingFooterPreItem(curPage, onSelectedPageCallback)

        // 最多显示10个item
        if (totalPage <= 10) {
            for (pageIndex in 1..totalPage) {
                PaingFooterNumItem(pageIndex, curPage, onSelectedPageCallback)
            }
        } else {  // 总页数大于10，需要按照一定的规则显示省略号
            if (curPage <= 5) {  //当前页码小于5，前8个正常显示，第9个显示省略号，第10个显示最后一个页码
                for (pageIndex in 1..8) {
                    PaingFooterNumItem(pageIndex, curPage, onSelectedPageCallback)
                }
                PaingFooterMoreItem()
                PaingFooterNumItem(totalPage, curPage, onSelectedPageCallback)
            } else if (curPage >= totalPage - 5) {   //当前页码大于倒数第五个页码，后8个正常显示，第1个显示省略号，第1个显示第一个页码
                PaingFooterNumItem(1, curPage, onSelectedPageCallback)
                PaingFooterMoreItem()
                for (pageIndex in totalPage - 8..totalPage) {
                    PaingFooterNumItem(pageIndex, curPage, onSelectedPageCallback)
                }
            } else {  // 否则第1个显示第一个页码，第2个显示省略号，第9个显示省略号，第10个显示最后一个页码，其余6个以当前页码为中心，显示相邻第6个页码
                PaingFooterNumItem(1, curPage, onSelectedPageCallback)
                PaingFooterMoreItem()
                for (pageIndex in curPage - 2..curPage + 3) {
                    PaingFooterNumItem(pageIndex, curPage, onSelectedPageCallback)
                }
                PaingFooterMoreItem()
                PaingFooterNumItem(totalPage, curPage, onSelectedPageCallback)
            }
        }
        PaingFooterNextItem(totalPage, curPage, onSelectedPageCallback)

    }
}

@Composable
private fun PaingFooterNumItem(pageIndex: Int, curPage: Int, onSelectedPage: (curPage: Int) -> Unit) {
    Box(
        modifier = Modifier.padding(horizontal = 2.dp).height(28.dp).widthIn(min = 28.dp)
            .clip(RoundedCornerShape(2.dp))
            .onClick  {
                onSelectedPage.invoke(pageIndex)
            }
            .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(2.dp))
            .let {
                if (curPage == pageIndex) it.background(
                    AppColorsProvider.current.primary,
                    RoundedCornerShape(2.dp)
                ) else it
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            pageIndex.toString(),
            color = if (curPage == pageIndex) Color.White else AppColorsProvider.current.secondText,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

@Composable
private fun PaingFooterMoreItem() {
    Box(
        modifier = Modifier.padding(horizontal = 2.dp).size(28.dp)
            .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "...",
            color = AppColorsProvider.current.secondText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PaingFooterPreItem(curPage: Int, onSelectedPage: (curPage: Int) -> Unit) {
    Box(
        modifier = Modifier.padding(horizontal = 2.dp).size(28.dp)
            .clip(RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(2.dp))
            .let {
                if (curPage > 1) it.onClick  {
                    onSelectedPage.invoke(curPage - 1)
                } else it
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource("image/ic_more.webp"),
            tint = if (curPage == 1) AppColorsProvider.current.divider else AppColorsProvider.current.secondIcon,
            contentDescription = "",
            modifier = Modifier.size(14.dp).rotate(180f)
        )
    }
}

@Composable
private fun PaingFooterNextItem(totalNum: Int, curPage: Int, onSelectedPage: (curPage: Int) -> Unit) {
    Box(
        modifier = Modifier.padding(horizontal = 2.dp).size(28.dp)
            .clip(RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, color = AppColorsProvider.current.divider), RoundedCornerShape(2.dp))
            .let {
                if (curPage < totalNum) it.onClick  {
                    onSelectedPage.invoke(curPage + 1)
                } else it
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource("image/ic_more.webp"),
            tint = if (curPage == totalNum) AppColorsProvider.current.divider else AppColorsProvider.current.secondIcon,
            contentDescription = "",
            modifier = Modifier.size(14.dp)
        )
    }
}