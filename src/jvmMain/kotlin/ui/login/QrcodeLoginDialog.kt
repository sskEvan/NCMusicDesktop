package ui.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import base.UserManager
import com.google.gson.Gson
import com.lt.load_the_image.rememberImagePainter
import http.NCRetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import util.QrcodeUtil
import kotlinx.coroutines.launch
import model.LoginResult
import moe.tlaster.precompose.ui.viewModel
import ui.common.LoadingComponent
import ui.common.NoSuccessComponent
import ui.common.theme.AppColorsProvider
import viewmodel.BaseViewModel
import java.io.File

@Composable
fun QrcodeLoginDialog(show: MutableState<Boolean>) {
    Dialog(
        onCloseRequest = { show.value = false }, visible = show.value,
        state = rememberDialogState(size = DpSize(400.dp, 480.dp)),
        resizable = false,
        title = ""
    ) {
        if (show.value) {
            QrcodeLoginDialogContent(show)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun QrcodeLoginDialogContent(show: MutableState<Boolean>) {
    val viewModel: LoginViewModel = viewModel { LoginViewModel() }

    LaunchedEffect(Unit) {
        viewModel.qrcodeAuth()
    }
    LaunchedEffect(viewModel.qrcodeAuthStatus) {
        if (viewModel.qrcodeAuthStatus == 800) {  // 二维码过期，重走认证流程
            println("----二维码过期，重新生成")
            viewModel.qrcodeAuth()
        }
    }
    LaunchedEffect(viewModel.getAccountInfoSuccess) {
        if (viewModel.getAccountInfoSuccess == true) {
            show.value = false
        } else if (viewModel.getAccountInfoSuccess == false) {  // 获取用户信息失败，重走认证流程
            viewModel.qrcodeAuth()
        }
    }


    var focusState by remember { mutableStateOf(false) }
    val totalWidth = remember { 400.dp }
    val smallWidth = remember { 180.dp }
    val largeWidth = remember { 220.dp }

    val imageInitOffset = remember { (totalWidth - largeWidth) / 2 + largeWidth - smallWidth }
    val imageTargetOffset = remember { (totalWidth - smallWidth * 2) / 2 }
    val qrcodeInitOffset = remember { (totalWidth - largeWidth) / 2 }
    val qrcodeTargetOffset = remember { totalWidth - (totalWidth - smallWidth * 2) / 2 - smallWidth }

    val scope = rememberCoroutineScope()
    val offsetAnim = remember { Animatable(0f) }

    Column(
        modifier = Modifier.height(480.dp).background(AppColorsProvider.current.pure).padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            "扫码登录",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = AppColorsProvider.current.firstText
        )

        Box(modifier = Modifier.width(totalWidth)
            .onPointerEvent(PointerEventType.Enter) {
                focusState = true
                scope.launch {
                    offsetAnim.animateTo(1f, tween(600))
                }
            }
            .onPointerEvent(PointerEventType.Exit) {
                focusState = false
                scope.launch {
                    offsetAnim.animateTo(0f, tween(600))
                }
            },
            contentAlignment = Alignment.CenterStart
        ) {
            CpnScanTipImage(
                Modifier.width(smallWidth).height(260.dp).padding(12.dp)
                    .scale(offsetAnim.value * 0.3f + 0.7f)
                    .offset(imageInitOffset + (imageTargetOffset - imageInitOffset) * offsetAnim.value)
                    .alpha(offsetAnim.value)
            )

            val cpnScanQrcodeWidth = largeWidth - (largeWidth - smallWidth) * offsetAnim.value

            CpnScanQrcode(
                Modifier.width(cpnScanQrcodeWidth)
                    .height(300.dp)
                    .offset(qrcodeInitOffset + (qrcodeTargetOffset - qrcodeInitOffset) * offsetAnim.value)
                    .padding(top = 20.dp),
                cpnScanQrcodeWidth
            )
        }
    }
}

@Composable
private fun CpnScanTipImage(modifier: Modifier) {
    Image(painterResource("image/ic_scan_code_tip.webp"), contentDescription = "扫描二维码提示", modifier = modifier)
}

@Composable
private fun CpnScanQrcode(modifier: Modifier, qrcodeSize: Dp) {
    val viewModel: LoginViewModel = viewModel { LoginViewModel() }
    val tip = when (viewModel.qrcodeAuthStatus) {
        801, 802 -> "请使用网易云音乐APP\n扫码登录"
        803 -> "正在获取用户信息..."
        null -> "正在加载二维码"
        else -> "加载二维码出错"
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.qrcodeAuthStatus == 801 || viewModel.qrcodeAuthStatus == 802) {
            Image(
                rememberImagePainter(viewModel.qrcodeFile!!),
                contentDescription = "",
                modifier = Modifier.size(qrcodeSize)
            )
        } else {
            Box(
                modifier = Modifier.size(qrcodeSize),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.qrcodeAuthStatus == null) {
                    LoadingComponent()
                } else {
                    NoSuccessComponent(modifier = Modifier.wrapContentSize(), message = "") {
                        viewModel.qrcodeAuth()
                    }
                }
            }
        }

        Text(
            tip,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = AppColorsProvider.current.firstText,
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}

class LoginViewModel : BaseViewModel() {
    var qrcodeAuthStatus by mutableStateOf<Int?>(null)
    var qrcodeFile by mutableStateOf<File?>(null)
    var getAccountInfoSuccess by mutableStateOf<Boolean?>(null)
    private var mLastQrcodeAuthJob: Job? = null
    private var mCookie = ""

    fun qrcodeAuth() {
        mLastQrcodeAuthJob?.let {
            println("----重新授权，取消上次任务")
            it.cancel()
        }
        qrcodeAuthStatus = null
        mLastQrcodeAuthJob = launch(
            handleFailBlock = { code, msg ->
                println("----handleFailBlock")
                qrcodeAuthStatus = code
            }
        ) {
            println("----start getLoginQrcodeKey")
            val qrcodeKeyResult = NCRetrofitClient.getNCApi().getLoginQrcodeKey()
            println("----start getLoginQrcodeValue")
            val qrcodeValueResult = NCRetrofitClient.getNCApi().getLoginQrcodeValue(qrcodeKeyResult.data.unikey)
            println("----start createQrcodeFile")
            qrcodeFile = QrcodeUtil.createQrcodeFile(
                qrcodeValueResult.data.qrurl,
                500,
                500
            )
            println("----start checkQrcodeAuthStatus")
            var qrcodeAuthResult = NCRetrofitClient.getNCApi().checkQrcodeAuthStatus(qrcodeKeyResult.data.unikey)
            qrcodeAuthStatus = qrcodeAuthResult.code
            while (mLastQrcodeAuthJob?.isActive != false) {
                // 4s轮训一次登录授权状态
                delay(4000)
                qrcodeAuthResult = NCRetrofitClient.getNCApi().checkQrcodeAuthStatus(qrcodeKeyResult.data.unikey)
                qrcodeAuthStatus = qrcodeAuthResult.code
                if (qrcodeAuthResult.resultOk()) {  // 授权成功
                    println("----授权成功")
                    mCookie = qrcodeAuthResult.cookie
                    getAccountInfo()
                    break
                } else if (qrcodeAuthStatus == 800) {
                    println("----二维码过期")
                } else if (qrcodeAuthStatus == 801) {
                    println("----等待扫码")
                } else if (qrcodeAuthStatus == 802) {
                    println("----待确认")
                }
            }
            println("授权成功----请求个人信息和账户信息")
            qrcodeAuthResult
        }
    }

    private suspend fun getAccountInfo() {
        val accountInfoResult = NCRetrofitClient.getNCApi().getAccountInfo(mCookie)
        if (accountInfoResult.resultOk()) {
            val loginResult = LoginResult(accountInfoResult.account, accountInfoResult.profile, mCookie)
            UserManager.saveLoginResult(Gson().toJson(loginResult))
            getAccountInfoSuccess = true
        } else {
            getAccountInfoSuccess = false
        }
    }
}