package base.player

import model.SongBean


/**
 * Created by ssk on 2022/4/23.
 */
interface IPlayer {
    fun setDataSource(songBean: SongBean)
    fun start()
    fun pause()
    fun resume()
    fun stop()
    fun seekTo(position: Float)

    fun addListener(listener: IPlayerListener)
    fun removeListener(listener: IPlayerListener)
}