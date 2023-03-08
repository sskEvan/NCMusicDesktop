package ui.common.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import ui.common.theme.color.AppColors
import ui.common.theme.color.light.*


// 夜间模式
const val THEME_NIGHT = -1

// 默认主题
const val THEME_DEFAULT = 0

// 蓝色主题
const val THEME_BLUE = 1

// 绿色主题
const val THEME_GREEN = 2

// 橙色主题
const val THEME_ORIGIN = 3

// 紫色主题
const val THEME_PURPLE = 4

// 黄色主题
const val THEME_YELLOW = 5

/**
 * 主题状态
 */
val themeTypeState: MutableState<Int> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    mutableStateOf(getDefaultThemeId())
}

var AppColorsProvider = compositionLocalOf {
    DefaultColorPalette
}

/**
 * 获取当前默认主题
 */
fun getDefaultThemeId(): Int = THEME_DEFAULT

const val TWEEN_DURATION = 200


@Composable
@ReadOnlyComposable
fun isInDarkTheme(): Boolean {
    return isSystemInDarkTheme() || themeTypeState.value == THEME_NIGHT
}

@Composable
fun AppTheme(
    themeType: Int,
    isDark: Boolean = isInDarkTheme(),
    content: @Composable () -> Unit
) {

    val targetColors = if (isDark) DarkColorPalette else {
        when (themeType) {
            THEME_BLUE -> BlueColorPalette
            THEME_GREEN -> GreenColorPalette
            THEME_ORIGIN -> OriginColorPalette
            THEME_PURPLE -> PurpleColorPalette
            THEME_YELLOW -> YellowColorPalette
            else -> DefaultColorPalette
        }
    }

    val topBarColor = animateColorAsState(targetColors.topBarColor, TweenSpec(TWEEN_DURATION))
    val pure = animateColorAsState(targetColors.pure, TweenSpec(TWEEN_DURATION))
    val primary = animateColorAsState(targetColors.primary, TweenSpec(TWEEN_DURATION))
    val primaryVariant = animateColorAsState(targetColors.primaryVariant, TweenSpec(TWEEN_DURATION))
    val secondary = animateColorAsState(targetColors.secondary, TweenSpec(TWEEN_DURATION))
    val background = animateColorAsState(targetColors.background, TweenSpec(TWEEN_DURATION))
    val firstText = animateColorAsState(targetColors.firstText, TweenSpec(TWEEN_DURATION))
    val secondText = animateColorAsState(targetColors.secondText, TweenSpec(TWEEN_DURATION))
    val thirdText = animateColorAsState(targetColors.thirdText, TweenSpec(TWEEN_DURATION))
    val firstIcon = animateColorAsState(targetColors.firstIcon, TweenSpec(TWEEN_DURATION))
    val secondIcon = animateColorAsState(targetColors.secondIcon, TweenSpec(TWEEN_DURATION))
    val thirdIcon = animateColorAsState(targetColors.thirdIcon, TweenSpec(TWEEN_DURATION))
    val card = animateColorAsState(targetColors.card, TweenSpec(TWEEN_DURATION))
    val divider = animateColorAsState(targetColors.divider, TweenSpec(TWEEN_DURATION))

    val appColors = AppColors(
        topBar = topBarColor.value,
        pure = pure.value,
        primary = primary.value,
        primaryVariant = primaryVariant.value,
        secondary = secondary.value,
        background = background.value,
        firstText = firstText.value,
        secondText = secondText.value,
        thirdText = thirdText.value,
        firstIcon = firstIcon.value,
        secondIcon = secondIcon.value,
        thirdIcon = thirdIcon.value,
        card = card.value,
        divider = divider.value
    )

    CompositionLocalProvider(AppColorsProvider provides appColors) {
        MaterialTheme(
            shapes = shapes
        ) {
            content()
        }
    }

}

