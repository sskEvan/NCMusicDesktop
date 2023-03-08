package ui.main.cpn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CursorDropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.common.theme.*
import ui.common.theme.color.light.*

@Composable
fun CpnThemePopup(showPopupWindow: MutableState<Boolean>) {

    var lastSelectedThemeIndex = remember { 0 }

    val themeModels = remember {
        mutableStateListOf(
            mutableStateOf(ThemeModel("默认", THEME_DEFAULT, DefaultColorPalette.primary, true)),
            mutableStateOf(ThemeModel("夜间", THEME_NIGHT, DarkColorPalette.pure, false)),
            mutableStateOf(ThemeModel("蓝色", THEME_BLUE, BlueColorPalette.primary, false)),
            mutableStateOf(ThemeModel("绿色", THEME_GREEN, GreenColorPalette.primary, false)),
            mutableStateOf(ThemeModel("橙色", THEME_ORIGIN, OriginColorPalette.primary, false)),
            mutableStateOf(ThemeModel("紫色", THEME_PURPLE, PurpleColorPalette.primary, false)),
            mutableStateOf(ThemeModel("黄色", THEME_YELLOW, YellowColorPalette.primary, false)),
        )
    }

    CursorDropdownMenu(
        showPopupWindow.value,
        onDismissRequest = { showPopupWindow.value = false },
    ) {
        themeModels.forEachIndexed { index, themeModel ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clickable {
                        themeTypeState.value = themeModel.value.themeType
                        themeModels[lastSelectedThemeIndex].value = themeModels[lastSelectedThemeIndex].value.copy(selected = false)
                        lastSelectedThemeIndex = index
                        themeModels[index].value = themeModels[index].value.copy(selected = true)
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(50))
                        .background(themeModel.value.color)
                )

                Text(
                    text = themeModel.value.name,
                    modifier = Modifier.padding(start = 12.dp, end = 20.dp),
                    fontSize = 14.sp,
                    color = AppColorsProvider.current.secondText
                )

                if (themeModels[index].value.selected) {
                    Icon(
                        painterResource("image/ic_checked.webp"),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = themeModel.value.color
                    )
                }
            }
        }
    }

}

data class ThemeModel(val name: String, val themeType: Int, val color: Color, val selected: Boolean)