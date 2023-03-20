package base.player

/**
 * Created by ssk on 2022/4/23.
 */
sealed class PlayerStatus() {
    object IDLE: PlayerStatus()
    object PREPARED: PlayerStatus()
    object STARTED: PlayerStatus()
    object PAUSED: PlayerStatus()
    object STOPPED: PlayerStatus()
    object COMPLETED: PlayerStatus()
    class ERROR(val errorCode: Int, val errorMsg: String): PlayerStatus()
}

object PlayerErrorCode {
    // 环境错误，没有安装VLC组件
    const val ERROR_ENV_INVALID = 1
    // 获取歌曲播放URL错误
    const val ERROR_GET_URL = 1
    // 播放错误
    const val ERROR_PLAY = 2

}