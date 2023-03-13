package viewmodel

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import model.BaseResult
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


typealias ViewStateMutableStateFlow<T> = MutableStateFlow<ViewState<T>>

open class BaseViewModel : ViewModel() {
    protected fun <T : BaseResult> launchFlow(
        handleSuccessBlock: ((T) -> Unit)? = null,
        handleFailBlock: ((code: Int?, message: String?) -> Unit)? = null,
        judgeEmpty: ((T) -> Boolean)? = null,
        call: suspend () -> T
    ): ViewStateMutableStateFlow<T> {
        val flow = MutableStateFlow<ViewState<T>>(ViewState.Loading)

        viewModelScope.launch {
            runCatching {
                call()
            }.onSuccess { result ->
                if (result.resultOk()) {
                    if (judgeEmpty?.invoke(result) == true) {
                        flow.emit(ViewState.Empty)
                    } else {
                        handleSuccessBlock?.invoke(result)
                        flow.emit(ViewState.Success(result))
                    }
                } else {
                    handleFailBlock?.invoke(result.code ?: -1, result.message ?: "请求出错")
                    flow.emit(ViewState.Fail(result.code?.toString() ?: "-1", result.message ?: "请求出错"))
                }
            }.onFailure { e ->
                flow.emit(ViewState.Error(e))
            }
        }

        return flow
    }

    protected fun <T : BaseResult> launch(
        handleSuccessBlock: ((T) -> Unit)? = null,
        handleFailBlock: ((code: Int?, message: String?) -> Unit)? = null,
        call: suspend () -> T
    ) : Job {
        return viewModelScope.launch {
            runCatching {
                call()
            }.onSuccess { result ->
                if (result.resultOk()) {
                    handleSuccessBlock?.invoke(result)
                } else {
                    handleFailBlock?.invoke(result.code ?: -1, result.message ?: "请求出错")
                }
            }.onFailure { e ->
                handleFailBlock?.invoke(-1000, "请求出错")
               e.printStackTrace()
            }
        }
    }

}

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    object Empty : ViewState<Nothing>()
    data class Fail(val errorCode: String, val errorMsg: String) : ViewState<Nothing>()
    data class Error(val exception: Throwable) : ViewState<Nothing>()
}