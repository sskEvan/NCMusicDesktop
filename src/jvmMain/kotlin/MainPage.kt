import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun MainPage() {
    val showLoginDialog = rememberSaveable { mutableStateOf(false) }

    Box {
        Button(onClick = {
            showLoginDialog.value = true
        }) {
            Text("点击登录")
        }
    }
    QrcodeLoginDialog(showLoginDialog)
}