package ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import model.IBasePagingBean
import moe.tlaster.precompose.ui.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
import ui.playlist.cpn.CommentListViewModel
import base.ViewState
import base.ViewStateMutableStateFlow
import java.util.*

@Composable
fun <T : IBasePagingBean> ViewStateLazyGridPagingComponent(
    modifier: Modifier = Modifier,
    key: String = UUID.randomUUID().toString(),
    columns: Int,
    pageSize: Int = 20,
    loadDataBlock: (pageSize: Int, curPage: Int) -> ViewStateMutableStateFlow<T>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    scrollHeader: (@Composable () -> Unit)? = null,
    stickyHeader: (@Composable () -> Unit)? = null,
    viewStateComponentModifier: Modifier = Modifier.fillMaxWidth().heightIn(min = 320.dp),
    viewStateContentAlignment: Alignment = Alignment.Center,
    customLoadingComponent: @Composable (() -> Unit)? = null,
    customEmptyComponent: @Composable (() -> Unit)? = null,
    customFailComponent: @Composable ((errorMessage: String?, loadDataBlock: () -> Unit) -> Unit)? = null,
    customErrorComponent: @Composable ((errorMessage: Pair<String, String>, loadDataBlock: () -> Unit) -> Unit)? = null,
    gridContent: LazyGridScope.(data: T) -> Unit,
) {

    var height by remember { mutableStateOf(0) }

    SubComposeLazyList(
        modifier = Modifier.onGloballyPositioned {
            height = it.size.height
        },
        scrollHeader, stickyHeader
    ) { scrollHeaderHeight, stickyHeaderHeight ->
        val localDensity = LocalDensity.current
        // fix 当有scrollHeader和stickyHeader时，当列表显示滑动，并且通过stickyHeader切换数据源，造成列表滚动的bug
        val viewStateComponentModifier = remember(height) {
            if (height == 0) {
                viewStateComponentModifier
            } else {
                viewStateComponentModifier.height(height = ((height + scrollHeaderHeight + stickyHeaderHeight) / localDensity.density).dp)
            }
        }

        val vm = viewModel(listOf(key)) { ViewStateLazyListViewModel<T>() }
        val reloadFlag = vm.reloadFlag

        val flow = remember(reloadFlag, key) {
            if (reloadFlag == 0) {  // first load data
                if (vm.flow == null) {
                    vm.flow = loadDataBlock.invoke(pageSize, vm.curPage.value)
                }
            } else {  // retry load data when user trigger loadDataBlock
                vm.flow = loadDataBlock.invoke(pageSize, vm.curPage.value)
            }
            vm.flow!!
        }

        val viewState by flow.collectAsState()

        var showStickyHeader by remember { mutableStateOf(false) }
        val firstVisibleItemIndex by remember { derivedStateOf { lazyGridState.firstVisibleItemIndex } }

        LaunchedEffect(Unit) {
            snapshotFlow { firstVisibleItemIndex }
                .collect { firstVisibleItemIndex ->
                    if (stickyHeader != null) {
                        val minShowStickyIndex = if (scrollHeader == null) 0 else 1
                        showStickyHeader = firstVisibleItemIndex >= minShowStickyIndex
                    }
                }
        }

        Box {
            LazyVerticalGrid(
                GridCells.Fixed(columns),
                modifier,
                lazyGridState,
                contentPadding
            ) {
                if (scrollHeader != null) {
                    item(span = { GridItemSpan(columns) }) { scrollHeader() }
                }
                if (stickyHeader != null) {
                    item(span = { GridItemSpan(columns) }) {
                        stickyHeader()
                    }
                }
                when (viewState) {
                    is ViewState.Loading -> {
                        item(span = { GridItemSpan(columns) }) {
                            if (customLoadingComponent != null) {
                                customLoadingComponent.invoke()
                            } else {
                                LoadingComponent(
                                    modifier = viewStateComponentModifier,
                                    contentAlignment = viewStateContentAlignment
                                )
                            }
                        }
                    }


                    is ViewState.Empty -> {
                        item(span = { GridItemSpan(columns) }) {
                            if (customEmptyComponent != null) {
                                customEmptyComponent.invoke()
                            } else {
                                NoSuccessComponent(
                                    contentAlignment = viewStateContentAlignment,
                                    modifier = viewStateComponentModifier,
                                ) {
                                    vm.reload()
                                }
                            }
                        }
                    }

                    is ViewState.Fail -> {
                        item(span = { GridItemSpan(columns) }) {
                            if (customFailComponent != null) {
                                customFailComponent.invoke(
                                    "错误码：${(viewState as ViewState.Fail).errorCode}；${(viewState as ViewState.Fail).errorMsg}，点我重试",
                                ) {
                                    vm.reload()
                                }
                            } else {
                                NoSuccessComponent(
                                    modifier = viewStateComponentModifier,
                                    message = "错误码：${(viewState as ViewState.Fail).errorCode}；${(viewState as ViewState.Fail).errorMsg}，点我重试",
                                    contentAlignment = viewStateContentAlignment
                                ) {
                                   vm.reload()
                                }
                            }
                        }
                    }

                    is ViewState.Error -> {
                        item(span = { GridItemSpan(columns) }) {
                            if (customErrorComponent != null) {
                                customErrorComponent.invoke(
                                    getErrorMessagePair((viewState as ViewState.Error).exception),
                                ) {
                                    vm.reload()
                                }
                            } else {
                                val errorMessagePair = getErrorMessagePair((viewState as ViewState.Error).exception)
                                NoSuccessComponent(
                                    modifier = viewStateComponentModifier,
                                    message = errorMessagePair.first,
                                    iconResId = errorMessagePair.second,
                                    contentAlignment = viewStateContentAlignment,
                                ) {
                                    vm.reload()
                                }
                            }
                        }
                    }

                    is ViewState.Success -> {
                        val data = (viewState as ViewState.Success<T>).data
                        gridContent(data)

                        // 底部分页组件
                        if (data.getTotalCount() > CommentListViewModel.pageSize) {
                            item(span = { GridItemSpan(columns) }) {
                                PaingFooterNumBar(data.getTotalCount(), pageSize, vm.curPage.value) {
                                    vm.curPage.value = it
                                    vm.reload()
                                }
                            }
                        }
                    }
                }
            }
            if (showStickyHeader) {
                Box(
                    modifier = Modifier.padding(
                        start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
                        end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                    )
                ) {
                    stickyHeader?.invoke()
                }
            }
        }

    }
}

class ViewStateLazyListViewModel<T> : ViewModel() {
    var flow: ViewStateMutableStateFlow<T>? = null
    var reloadFlag by mutableStateOf(0)
        private set

    var curPage = mutableStateOf(1)

    fun reload() {
        reloadFlag++
    }
}


@Composable
private fun SubComposeLazyList(
    modifier: Modifier,
    scrollHeader: (@Composable () -> Unit)? = null,
    stickyHeader: (@Composable () -> Unit)? = null,
    content: @Composable (scrollHeader: Float, stickyHeader: Float) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
            .clipToBounds()
    ) { constraints ->
        var scrollHeaderPlaceable: Placeable? = null
        scrollHeader?.let {
            scrollHeaderPlaceable = subcompose("scrollHeader", scrollHeader).first().measure(constraints)
        }
        var stickyHeaderPlaceable: Placeable? = null
        stickyHeader?.let {
            stickyHeaderPlaceable = subcompose("stickyHeader", stickyHeader).first().measure(constraints)
        }
        val contentPlaceable = subcompose("content") {
            content(
                scrollHeaderPlaceable?.height?.toFloat() ?: 0f,
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


fun <T> LazyListScope.handleListContent(
    viewState:  ViewState<T>?,
    reloadDataBlock: () -> Unit,
    viewStateComponentModifier: Modifier = Modifier.fillMaxWidth().heightIn(min = 320.dp),
    callback: LazyListScope.(data: T) -> Unit,
) {

    when (viewState) {
        is ViewState.Empty -> {
            item {
                NoSuccessComponent(
                    modifier = viewStateComponentModifier,
                ) {
                    reloadDataBlock.invoke()
                }
            }
        }

        is ViewState.Fail -> {
            item {
                NoSuccessComponent(
                    modifier = viewStateComponentModifier,
                    message = "错误码：${viewState.errorCode}；${viewState.errorMsg}，点我重试",
                ) {
                   reloadDataBlock.invoke()
                }
            }
        }

        is ViewState.Error -> {
            item {
                val errorMessagePair = getErrorMessagePair(viewState.exception)
                NoSuccessComponent(
                    modifier = viewStateComponentModifier,
                    message = errorMessagePair.first,
                    iconResId = errorMessagePair.second,
                ) {
                    reloadDataBlock.invoke()

                }
            }
        }

        is ViewState.Success -> {
            val data = viewState.data
            callback(data)
        }
         else -> {
             item {
                 LoadingComponent(viewStateComponentModifier)
             }
         }
    }
}


