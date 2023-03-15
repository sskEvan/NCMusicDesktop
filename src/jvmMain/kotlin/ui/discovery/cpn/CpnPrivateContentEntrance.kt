package ui.discovery.cpn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.load_the_image.rememberImagePainter
import ui.common.TableLayout
import http.NCRetrofitClient
import model.PrivateContentItem
import moe.tlaster.precompose.ui.viewModel
import ui.common.CpnActionMore
import ui.common.ViewStateComponent
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel

/**
 * 独家放送入口
 */
@Composable
fun CpnPrivateContentEntrance(onClickMore: () -> Unit) {
    val viewModel = viewModel { CpnPrivateContentViewModel() }

    Column {
        CpnActionMore("独家放送") {
            onClickMore.invoke()
        }

        ViewStateComponent(
            key = "CpnPrivateContentEntrance",
            loadDataBlock = { viewModel.getPrivateContent() }) { data ->
            Content(data.result)
        }
    }
}

@Composable
private fun Content(list: List<PrivateContentItem>) {
    TableLayout(modifier = Modifier.padding(horizontal = 10.dp), cellsCount = 4) {
        list.forEach {
            PrivateContentItem(it)
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PrivateContentItem(item: PrivateContentItem) {
    var focusState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
            focusState = true
        }.onPointerEvent(PointerEventType.Exit) {
            focusState = false
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                rememberImagePainter(item.picUrl),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(180.dp).height(100.dp).clip(RoundedCornerShape(6.dp))
            )

            Row(modifier = Modifier.padding(top = 6.dp, end = 6.dp).align(Alignment.TopEnd), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource("image/ic_play_count.webp"),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 6.dp).size(12.dp)
                )
            }

            if (focusState) {
                Icon(
                    painter = painterResource("image/ic_logo_play.webp"),
                    contentDescription = "",
                    modifier = Modifier.padding(top = 6.dp, start = 6.dp).size(32.dp),
                    tint = Color.White
                )
            }
        }

        Text(
            item.name,
            color = AppColorsProvider.current.firstText,
            fontSize = 12.sp,
            maxLines = 2,
            modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp).height(48.dp)
        )
    }
}

class CpnPrivateContentViewModel : BaseViewModel() {

    fun getPrivateContent() = launchFlow {
        println("获取独家放送...")
        NCRetrofitClient.getNCApi().getPrivateContent()
    }

}