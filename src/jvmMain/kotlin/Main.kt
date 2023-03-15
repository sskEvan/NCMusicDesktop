import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import base.AppConfig
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.rememberNavigator
import router.NCNavigatorManager
import ui.common.theme.AppTheme
import ui.common.theme.themeTypeState
import ui.main.MainPage
import ui.todo.TestPage
import java.awt.Dimension

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(AppConfig.windowMinWidth, AppConfig.windowMinHeight))
    AppConfig.windowState = windowState
    PreComposeWindow(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = ""
    ) {
        window.minimumSize = Dimension(AppConfig.windowMinWidth.value.toInt(), AppConfig.windowMinHeight.value.toInt())
        window.rootPane.apply {
            rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
        }
        window.size
        App()
    }
}

@Composable
@Preview
private fun App() {
    AppTheme(themeTypeState.value) {
        NCNavigatorManager.navigator = rememberNavigator()
        MainPage()
    }
}
