import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import moe.tlaster.precompose.PreComposeWindow
import ui.common.theme.AppTheme
import ui.common.theme.themeTypeState
import ui.main.MainPage
import java.awt.Dimension

fun main() = application {
    PreComposeWindow(
        state = rememberWindowState(size = DpSize(1000.dp, 660.dp)),
        onCloseRequest = ::exitApplication,
        title = ""
    ) {
        window.minimumSize = Dimension(1000.dp.value.toInt(), 660.dp.value.toInt())
        window.rootPane.apply {
            rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
        }
        App()
    }
}

@Composable
@Preview
private fun App() {
    AppTheme(themeTypeState.value) {
        MainPage()
    }
}
