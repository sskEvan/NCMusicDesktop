package base.player

import http.NCRetrofitClient
import kotlinx.coroutines.*
import model.SongBean
import java.util.*
import kotlin.collections.ArrayList

object NCPlayer : IPlayer {


    private var mStatus: PlayerStatus = PlayerStatus.IDLE
    var mCurSongBean: SongBean? = null
    private val mTimer: Timer = Timer()
    private var mUpdateDuringTask: TimerTask? = null
    private val mListeners = ArrayList<IPlayerListener>()
    private var mJob: Job? = null

    override fun setDataSource(songBean: SongBean) {
        mCurSongBean = songBean
    }

    override fun start() {
    }

    private fun getSongUrlAndPlay(songId: Long) {
        mJob?.cancel()
        mJob = GlobalScope.launch(context = Dispatchers.IO) {
            try {
                val url = NCRetrofitClient.getNCApi().getSongUrl(songId).data.firstOrNull()?.url
                    ?: "https://music.163.com/song/media/outer/url?id=$songId.mp3"

            } catch (e: Exception) {
                if (e !is CancellationException) {
                    println("getSongUrlAndPlay e = $e")
                    e.printStackTrace()
                    mListeners.forEach {
                        it.onStatusChanged(PlayerStatus.ERROR)
                    }
                }
            }
        }
    }


    override fun pause() {
    }

    override fun resume() {
    }

    override fun stop() {
    }

    override fun seekTo(position: Int) {
    }
}