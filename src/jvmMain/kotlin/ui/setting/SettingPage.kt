package ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.common.theme.AppColorsProvider
import ui.main.cpn.CommonTitleBar

@Composable
fun SettingPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        CommonTitleBar("设置", true)
        Text(
            text = "声明",
            modifier = Modifier.padding(20.dp),
            fontSize = 18.sp,
            color = AppColorsProvider.current.firstText,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "本应用非 网易云音乐 官方产品，内部所有资源来自互联网，仅作学习分享使用，他人如何使用此应用与本应用无关。",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            color = AppColorsProvider.current.secondText,
        )
    }
}