package ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ui.common.theme.AppColorsProvider

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    defaultLine: Int = 2,
    text: String = "",
    color: Color = AppColorsProvider.current.secondText,
    fontSize: TextUnit = 12.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
) {

    //如果小于 defaultLine
    var isLessDefaultLine by remember {
        mutableStateOf(false)
    }

    var expand by remember {
        mutableStateOf(false)
    }

    var subContent by remember(text) {
        mutableStateOf(text)
    }

    var measureLineCount by remember(text) { mutableStateOf(false) }

    val anim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val content = remember(measureLineCount, expand, text) {
        if (expand) text else subContent
    }

    Row(
        modifier.animateContentSize()
    ) {

        Text(
            text = content,
            modifier = Modifier.padding(end = 10.dp).weight(1f),
            maxLines = if (expand) Int.MAX_VALUE else defaultLine,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            onTextLayout = { textLayoutResult ->

                if (textLayoutResult.lineCount <= defaultLine && !expand) {
                    val hasVisualOverflow = textLayoutResult.hasVisualOverflow
                    if (!measureLineCount) {
                        isLessDefaultLine = !hasVisualOverflow
                    }

                    if (hasVisualOverflow) {
                        val lastCharIndex = textLayoutResult.getLineEnd(defaultLine - 1, true)
                        //截取 Less状态的内容
                        val substring = content.substring(0, lastCharIndex)
                        subContent = substring.substring(0, substring.length) + "..."
                    }
                }
                measureLineCount = true

            },
        )

        if (!isLessDefaultLine) {
            Icon(
                painter = painterResource("image/ic_triangle_up.webp"),
                contentDescription = null,
                modifier = Modifier.clip(RoundedCornerShape(50)).onClick  {
                    expand = !expand
                    scope.launch {
                        anim.animateTo(if (expand) 1f else 0f)
                    }
                }
                    .size(24.dp)
                    .padding(6.dp)
                    .rotate(
                        anim.value * 180
                    ),
                tint = AppColorsProvider.current.secondText
            )
        }
    }

}
