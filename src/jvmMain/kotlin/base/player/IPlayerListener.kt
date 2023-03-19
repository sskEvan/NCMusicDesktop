package base.player


/**
 * Created by ssk on 2022/4/23.
 */
interface IPlayerListener {
    fun onStatusChanged(status: PlayerStatus)
    fun onProgress(totalDuring: Int, currentPosition: Int, percentage: Float)
}