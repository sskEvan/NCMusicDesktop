package ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import base.AppConfig
import ui.main.cpn.CpnMainButtomBar
import ui.main.cpn.CpnMainLeftMenu
import ui.main.cpn.CpnMainMusicPlayContainer
import ui.main.cpn.CpnMainRightContainer


@Composable
fun MainPage() {

    Column {
        Box(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxSize()) {
                CpnMainLeftMenu()
                CpnMainRightContainer()
            }
            Column {
                Spacer(modifier = Modifier.padding(top = AppConfig.topBarHeight))
                CpnMainMusicPlayContainer()
            }
        }

        CpnMainButtomBar()
    }

}