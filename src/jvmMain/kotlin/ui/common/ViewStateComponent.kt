package ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.JsonParseException
import moe.tlaster.precompose.ui.viewModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.compose.viewModel
import viewmodel.BaseViewModel
import viewmodel.ViewState
import viewmodel.ViewStateMutableStateFlow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.UUID

/**
 * Description->页面状态切换组件, 根据viewStateLiveData，自动切换各种状态页面
 * @param modifier：页面布局修饰
 * @param key: 用于维护ViewStateComponentViewModel中的flow,避免页面重建后，数据重新加载
 * @param initFlow: 初始化数据流，使用场景：某页面有需要显示popup，popup的内容来自网络，而且进入页面后可以预加载数据，等到用户点击显示popup时，数据以及加载完成
 * @param loadDataBlock：数据加载块
 * @param viewStateComponentModifier: 状态页面修饰
 * @param viewStateContentAlignment：状态页面居中方式
 * @param customEmptyComponent：自定义空布局,没设置则使用默认空布局
 * @param customFailComponent：自定义失败布局,没设置则使用默认失败布局
 * @param customErrorComponent：自定义错误布局,没设置则使用默认错误布局
 * @param contentView：正常页面内容
 */
@Composable
fun <T> ViewStateComponent(
    modifier: Modifier = Modifier,
    key: String = UUID.randomUUID().toString(),
    initFlow: ViewStateMutableStateFlow<T>? = null,
    loadDataBlock: (() -> ViewStateMutableStateFlow<T>),
    viewStateComponentModifier: Modifier = Modifier.fillMaxWidth().heightIn(min = 320.dp),
    viewStateContentAlignment: Alignment = Alignment.Center,
    customLoadingComponent: @Composable (() -> Unit)? = null,
    customEmptyComponent: @Composable (() -> Unit)? = null,
    customFailComponent: @Composable ((errorMessage: String?, retryFlag: MutableState<Int>, loadDataBlock: (() -> ViewStateMutableStateFlow<T>)) -> Unit)? = null,
    customErrorComponent: @Composable ((errorMessage: Pair<String, String>, retryFlag: MutableState<Int>, loadDataBlock: (() -> ViewStateMutableStateFlow<T>)) -> Unit)? = null,
    contentView: @Composable BoxScope.(data: T) -> Unit
) {

    val successData = remember { mutableStateOf<T?>(null) }
    val retryFlag = remember { mutableStateOf(0) }
    val vm = viewModel(listOf(key)) { ViewStateComponentViewModel<T>() }

    val flow = remember(retryFlag.value) {
        if (retryFlag.value == 0) {
            if (vm.flow == null) {
                vm.flow = initFlow ?: loadDataBlock.invoke()
            }
         } else {
            vm.flow = loadDataBlock.invoke()
        }
        vm.flow!!
    }
    val viewState by flow.collectAsState()
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (viewState) {
            is ViewState.Loading -> {
                if (customLoadingComponent != null) {
                    customLoadingComponent.invoke()
                } else {
                    LoadingComponent(
                        modifier = viewStateComponentModifier,
                        contentAlignment = viewStateContentAlignment
                    )
                }
            }

            is ViewState.Success -> {
                successData.value = (viewState as ViewState.Success<T>).data!!
                contentView(successData.value!!)
            }

            is ViewState.Empty -> {
                if (customEmptyComponent != null) {
                    customEmptyComponent.invoke()
                } else {
                    NoSuccessComponent(
                        contentAlignment = viewStateContentAlignment,
                        modifier = viewStateComponentModifier,
                    ) {
                        retryFlag.value++
                        loadDataBlock.invoke()
                    }
                }
            }

            is ViewState.Fail -> {
                if (customFailComponent != null) {
                    customFailComponent.invoke("错误码：${(viewState as ViewState.Fail).errorCode}；${(viewState as ViewState.Fail).errorMsg}，点我重试",
                        retryFlag,
                        loadDataBlock)
                } else {
                    NoSuccessComponent(
                        modifier = viewStateComponentModifier,
                        message = "错误码：${(viewState as ViewState.Fail).errorCode}；${(viewState as ViewState.Fail).errorMsg}，点我重试",
                        contentAlignment = viewStateContentAlignment
                    ) {
                        retryFlag.value++
                        loadDataBlock.invoke()
                    }
                }
            }

            is ViewState.Error -> {
                if (customErrorComponent != null) {
                    customErrorComponent.invoke(getErrorMessagePair((viewState as ViewState.Error).exception), retryFlag, loadDataBlock)
                } else {
                    val errorMessagePair = getErrorMessagePair((viewState as ViewState.Error).exception)
                    NoSuccessComponent(
                        modifier = viewStateComponentModifier,
                        message = errorMessagePair.first,
                        iconResId = errorMessagePair.second,
                        contentAlignment = viewStateContentAlignment,
                    ) {
                        retryFlag.value++
                        loadDataBlock.invoke()
                    }
                }
            }
        }
    }
}

class ViewStateComponentViewModel<T> : ViewModel() {
    var flow : ViewStateMutableStateFlow<T>? = null
}


fun getErrorMessagePair(exception: Throwable): Pair<String, String> {
    return when (exception) {
        is ConnectException,
        is UnknownHostException -> {
            Pair("网络连接失败", "image/ic_network_error.xml")
        }

        is SocketTimeoutException -> {
            Pair("网络连接超时", "image/ic_network_error.xml")
        }

        is JsonParseException -> {
            Pair("数据解析错误", "image/ic_network_error.xml")
        }

        else -> {
            Pair("未知错误", "image/ic_network_error.xml")
        }
    }
}


@Composable
fun <T> ViewState<T>.handleSuccess(callback: @Composable (data: T) -> Unit) {
    if (this is ViewState.Success) {
        callback.invoke(this.data)
    }
}
