import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*

@Composable
fun MainPage() {

    var clickMenu by remember { mutableStateOf("发现音乐") }
    Row {
        CpnMainLeftMenu() {
            clickMenu = it
        }
        CpnMainRightContent(clickMenu)
    }

}