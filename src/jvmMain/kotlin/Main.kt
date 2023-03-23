import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import base.AppConfig
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.rememberNavigator
import org.succlz123.lib.imageloader.core.ImageLoader
import router.NCNavigatorManager
import ui.common.theme.AppTheme
import ui.common.theme.themeTypeState
import ui.main.MainPage
import ui.main.cpn.CpnWindowsPlaformDecoratedButtons
import util.EnvUtil
import java.awt.Dimension
import java.io.File

fun main() = application {
    initImageLoader()
    val windowState = rememberWindowState(size = DpSize(AppConfig.windowMinWidth, AppConfig.windowMinHeight))
    PreComposeWindow(
        state = windowState,
        onCloseRequest = ::exitApplication,
        undecorated = EnvUtil.isWindows(),
        title = ""
    ) {
        window.minimumSize = Dimension(AppConfig.windowMinWidth.value.toInt(), AppConfig.windowMinHeight.value.toInt())
        window.rootPane.apply {
            rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
        }
        App()
        CpnWindowsPlaformDecoratedButtons(windowState)
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

private fun initImageLoader() {
    ImageLoader.configuration(rootDirectory = File(AppConfig.cacheRootDir))
}
