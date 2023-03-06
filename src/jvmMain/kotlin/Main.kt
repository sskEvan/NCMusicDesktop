import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        title = "NCMusicDesktop") {
        App()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {

    MaterialTheme {
        MainPage()
    }
}
