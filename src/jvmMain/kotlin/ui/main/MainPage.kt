package ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import router.NavGraph
import ui.main.cpn.CpnMainButtomBar
import ui.main.cpn.CpnMainLeftMenu
import ui.main.cpn.CpnMainRightContainer

@Composable
fun MainPage() {

    Column {
        Row(modifier = Modifier.weight(1f)) {
            CpnMainLeftMenu()
            NavGraph()
        }
        CpnMainButtomBar()
    }

}