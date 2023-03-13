package model

import androidx.annotation.Keep

@Keep
data class QrcodeKeyResult(val data: QrcodeKeyBean): BaseResult()

@Keep
data class QrcodeValueResult(val data: QrcodeValueBean): BaseResult()

@Keep
data class QrcodeKeyBean(val unikey: String)

@Keep
data class QrcodeValueBean(val qrurl: String, val qrimg: String?)

@Keep
data class QrcodeAuthResult(val cookie: String): BaseResult() {
    override fun resultOk(): Boolean {
        return code == 803
    }
}

@Keep
data class AccountInfoResult(val account: AccountBean, val profile: ProfileBean): BaseResult()

@Keep
data class LoginResult(
    val account: AccountBean,
    val profile: ProfileBean,
    val cookie: String
)

@Keep
data class AccountBean(
    val id: Long,
    val userName: String,
    val type: Int,
    val status: Int,
    val whitelistAuthority: Int,
    val createTime: Long,
    val tokenVersion: Int,
    val ban: Int,
    val baoyueVersion: Int,
    val donateVersion: Int,
    val vipType: Int,
    val viptypeVersion: Double,
    val anonimousUser: Boolean
)

@Keep
data class ProfileBean(
    val followed: Boolean,
    val userId: Int,
    val defaultAvatar: Boolean,
    val avatarUrl: String?,
    val nickname: String,
    val birthday: Long,
    val province: Int,
    val accountStatus: Int,
    val vipType: Int,
    val gender: Int,
    val djStatus: Int,
    val mutual: Boolean,
    val authStatus: Int,
    val backgroundImgId: Long,
    val userType: Int,
    val city: Int,
    val backgroundUrl: String?,
    val followeds: Int,
    val follows: Int,
    val eventCount: Int,
    val playlistCount: Int,
    val playlistBeSubscribedCount: Int
)
