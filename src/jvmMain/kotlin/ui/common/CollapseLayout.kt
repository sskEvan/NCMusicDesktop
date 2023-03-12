package ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import util.convertDp

@Composable
fun CollapseLayout(
    collapseHeader: @Composable () -> Unit,
    fixHeader: @Composable () -> Unit,
    stickyHeader: (@Composable () -> Unit)? = null,
    variableCollapseHeaderHeight: Boolean = false,
    state: CollapseState = rememberCollapseStateState(0f),
    content: @Composable () -> Unit,
) {

    SubComposeCollapseLayout(
        modifier = Modifier,
        collapseHeader,
        fixHeader,
        stickyHeader
    ) { collapseHeaderHeight, fixHeaderHeight, stickyHeaderHeight ->
        println("collapseHeaderHeight=$collapseHeaderHeight, fixHeaderHeight=$fixHeaderHeight, stickyHeaderHeight=$stickyHeaderHeight")
        val localDensity = LocalDensity.current
        val scope = rememberCoroutineScope()
        val scrollConnection = remember {
            CollapseNestedScrollConnection(
                state,
                scope,
//                800f,
//                200f,
//                200f,
                collapseHeaderHeight,
                fixHeaderHeight,
                stickyHeaderHeight
            )
        }
        val curOffset = scrollConnection.offset
        Box(modifier = Modifier
            .nestedScroll(scrollConnection)) {
            Column(modifier = Modifier.offset(y = curOffset.convertDp(localDensity))) {
                collapseHeader()
                stickyHeader?.invoke()
                content()
            }
            fixHeader()
        }
    }
}


class CollapseNestedScrollConnection(
    private val state: CollapseState,
    private val scope: CoroutineScope,
    private val collapseHeaderHeight: Float,
    private val fixHeaderHeight: Float,
    private val stickyHeaderHeight: Float
) : NestedScrollConnection {

    val maxScrollDistance = collapseHeaderHeight - fixHeaderHeight

    private val offsetAnim = Animatable(0f)
    private val maxScroll by derivedStateOf { offset <= -maxScrollDistance }
    private val overMiddle by derivedStateOf { offset <= -maxScrollDistance / 2 }
    private val minScrollSource by derivedStateOf { offset >= 0 }

    private var isAnim = false

    val offset get() = offsetAnim.value

    /**
     * 父布局先处理
     */
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (source == NestedScrollSource.Drag) {
            if (available.y < 0) {  // 向上滑动
                if (!maxScroll) {
                    snapToOffset(Math.max(offset + available.y, -maxScrollDistance))
                    return available
                }
            }
        }
        return super.onPreScroll(available, source)
    }

    /**
     * 父布局处理子布局处理后的事件
     */
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (source == NestedScrollSource.Drag) {
            if (available.y > 0) {  // 向下滑动
                if (!minScrollSource) {
                    snapToOffset(Math.min(offset + available.y, 0f))
                    return available
                }
            }
        }
        return super.onPostScroll(consumed, available, source)
    }

    /**
     * 父布局先处理
     */
    override suspend fun onPreFling(available: Velocity): Velocity {
        println("onPreFling,$available,offset=$offset")
//        if (available.y > 0) {  // 向下滑动
//            animateToOffset(-maxScrollDistance)
//            return available
//        }
        if (available.y < -100 && !maxScroll) {  // 向上滑动
            animateToOffset(-maxScrollDistance)
            return available
        }
        return super.onPreFling(available)

    }

    /**
     * 父布局处理子布局处理后的事件
     */
    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        println("onPostFling=$consumed,$available,offset=$offset")
//        if (available.y < 0) {  // 向上滑动
//            animateToOffset(0f)
//            return available
//        }
        if (available.y > 100 && !minScrollSource) {  // 向下滑动
            animateToOffset(0f)
            return available
        }
        return super.onPostFling(consumed, available)
    }

    private fun snapToOffset(value: Float) {
        isAnim = false
        scope.launch {
            offsetAnim.snapTo(value)
            updateCollapseState()
        }
    }

    internal fun animateToOffset(targetOffset: Float) {
        isAnim = true
        println("animateToOffset to $targetOffset")
        scope.launch {
            println("animateToOffset to $targetOffset done")
            offsetAnim.animateTo(targetOffset, tween(150)) {
                updateCollapseState()
            }
            isAnim = false
            println("animateToOffset to $targetOffset finish")
        }
    }

    private fun updateCollapseState() {
        state.collapseHeaderHeight = collapseHeaderHeight
        state.fixHeaderHeight = fixHeaderHeight
        state.stickyHeaderHeight = stickyHeaderHeight
        state.progress = offset / maxScrollDistance
    }
}

//@Composable
//private fun SubComposeCollapseLayout(
//    collapseHeader: @Composable () -> Unit,
//    fixHeader: @Composable () -> Unit,
//    stickyHeader: (@Composable () -> Unit)? = null,
//    content: @Composable (collapseHeaderHeight: Float, fixHeaderHeight: Float, stickyHeaderHeight: Float) -> Unit,
//) {
//    SubcomposeLayout(modifier = Modifier.clipToBounds()) { constraints ->
//        val collapseHeaderPlaceable = subcompose("collapseHeader", collapseHeader).first().measure(constraints)
//        val fixHeaderPlaceable = subcompose("fixHeader", fixHeader).first().measure(constraints)
//        var stickyHeaderPlaceable: Placeable? = null
//        stickyHeader?.let {
//            stickyHeaderPlaceable = subcompose("stickyHeader", stickyHeader).first().measure(constraints)
//        }
//        val contentPlaceable = subcompose("content") {
//            content(
//                collapseHeaderPlaceable.height.toFloat(),
//                fixHeaderPlaceable.height.toFloat(),
//                stickyHeaderPlaceable?.height?.toFloat() ?: 0f
//            )
//        }.map {
//            it.measure(constraints)
//        }.first()
//        layout(contentPlaceable.width, contentPlaceable.height) {
//            contentPlaceable.placeRelative(0, 0)
//        }
//    }
//}

@Composable
private fun SubComposeCollapseLayout(
    modifier: Modifier,
    collapseHeader: (@Composable () -> Unit)? = null,
    fixHeader: (@Composable () -> Unit)? = null,
    stickyHeader: (@Composable () -> Unit)? = null,
    content: @Composable (collapseHeader: Float, fixHeader: Float, stickyHeader: Float) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
            .clipToBounds()
    ) { constraints ->
        var collapseHeaderPlaceable: Placeable? = null
        collapseHeader?.let {
            collapseHeaderPlaceable =
                subcompose("collapseHeader", collapseHeader).first().measure(constraints)
        }
        var fixHeaderPlaceable: Placeable? = null
        fixHeader?.let {
            fixHeaderPlaceable = subcompose("fixHeader", fixHeader).first().measure(constraints)
        }
        var stickyHeaderPlaceable: Placeable? = null
        stickyHeader?.let {
            stickyHeaderPlaceable =
                subcompose("stickyHeader", stickyHeader).first().measure(constraints)
        }
        val contentPlaceable = subcompose("content") {
            content(
                collapseHeaderPlaceable?.height?.toFloat() ?: 0f,
                fixHeaderPlaceable?.height?.toFloat() ?: 0f,
                stickyHeaderPlaceable?.height?.toFloat() ?: 0f,
            )
        }.map {
            it.measure(constraints)
        }.first()

        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.placeRelative(0, 0)
        }
    }
}


@Stable
class CollapseState(
    progress: Float
) {
    var collapseHeaderHeight: Float = 0f
    var fixHeaderHeight: Float = 0f
    var stickyHeaderHeight: Float = 0f

    var progress by mutableStateOf(progress)

    override fun toString(): String {
        return "progress-$progress,collapseHeaderHeight=$collapseHeaderHeight,fixHeaderHeight=$fixHeaderHeight,stickyHeaderHeight=$stickyHeaderHeight"
    }

}


@Composable
fun rememberCollapseStateState(
    progress: Float
): CollapseState {
    return remember {
        CollapseState(
            progress
        )
    }
}

