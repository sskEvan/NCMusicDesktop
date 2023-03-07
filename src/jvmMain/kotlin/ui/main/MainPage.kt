package ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ui.main.cpn.CpnMainButtomBar
import ui.main.cpn.CpnMainLeftMenu
import ui.main.cpn.CpnMainRightContainer

@Composable
fun MainPage() {

    var clickMenu by remember { mutableStateOf("发现音乐") }

    Column {
        Row(modifier = Modifier.weight(1f)) {
            CpnMainLeftMenu() {
                clickMenu = it
            }
            CpnMainRightContainer(clickMenu)
        }
        CpnMainButtomBar()
    }

}