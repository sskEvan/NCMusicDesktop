package ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.common.theme.AppColorsProvider
import ui.main.cpn.CommonTopBar

@Composable
fun TodoPage(tag: String) {
    Column {
        Box(Modifier.fillMaxSize().background(AppColorsProvider.current.pure),
            contentAlignment = Alignment.Center
        ) {
            Text("todo-${tag}", color = AppColorsProvider.current.firstText)
        }
    }

}