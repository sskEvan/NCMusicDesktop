package ui.common.toast

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.common.theme.AppColorsProvider
import java.util.concurrent.ConcurrentLinkedQueue

object ToastManager {

    const val LENGTH_SHORT = 3000L
    const val LENGTH_LONG = 5000L

    var toastMessage by mutableStateOf<ToastMessage?>(null)
    private val messageQueue = ConcurrentLinkedQueue<ToastMessage>()

    init {
        val loopThread = Thread {
            while (true) {
                Thread.sleep(50)
                if (!messageQueue.isEmpty()) {
                    val nextToastMessage = messageQueue.poll()
                    if (nextToastMessage != null) {
                        toastMessage = nextToastMessage
                        Thread.sleep(nextToastMessage.during)
                    }
                } else {
                    toastMessage = null
                }
            }
        }
        loopThread.start()
    }

    fun showToast(message: String?, during: Long = LENGTH_SHORT) {
        message?.let { msg ->
            if (messageQueue.size > 5) {
                messageQueue.clear()
            }
            messageQueue.add(ToastMessage(message, during))
        }
    }
}

data class ToastMessage(val message: String, val during: Long)

@Composable
fun BoxScope.Toast() {
    ToastManager.toastMessage?.message?.let {msg ->
        Box(
            modifier = Modifier.padding(horizontal = 50.dp).align(BiasAlignment(0f, 0.75f))
                .border(BorderStroke(1.dp, AppColorsProvider.current.divider), RoundedCornerShape(6.dp))
                .background(AppColorsProvider.current.pure.copy(0.9f), RoundedCornerShape(6.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(msg, color = AppColorsProvider.current.firstText, fontSize = 14.sp, textAlign = TextAlign.Center)
        }
    }
}