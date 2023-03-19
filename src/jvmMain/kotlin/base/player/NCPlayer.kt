package base.player

import http.NCRetrofitClient
import kotlinx.coroutines.*
import model.SongBean
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent
import kotlin.collections.ArrayList

/**
 * 音乐播放器,基于vlcj
 */
class NCPlayer : IPlayer {

    private var mStatus: PlayerStatus = PlayerStatus.IDLE
    private var mCurSongBean: SongBean? = null
    private val mListeners = ArrayList<IPlayerListener>()
    private var mJob: Job? = null
    private val mMediaPlayer = AudioPlayerComponent().mediaPlayer()
    private var mDuring: Int = 0
    private var mCurTime: Int = 0

    init {
        mMediaPlayer.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
            override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                super.mediaPlayerReady(mediaPlayer)
                println("----${mCurSongBean?.name}----mediaPlayerReady")
                innerStartPlay()
            }

            override fun finished(mediaPlayer: MediaPlayer) {
                super.finished(mediaPlayer)
                setStatus(PlayerStatus.COMPLETED)
                println("----${mCurSongBean?.name}----finished")
            }

            override fun timeChanged(mediaPlayer: MediaPlayer, newTime: Long) {
                super.timeChanged(mediaPlayer, newTime)
                mCurTime = newTime.toInt()
                updateProgress()
            }

            override fun opening(mediaPlayer: MediaPlayer?) {
                super.opening(mediaPlayer)
                println("----${mCurSongBean?.name}----opening")
            }

            override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {
                super.buffering(mediaPlayer, newCache)
//                println("----${mCurSongBean?.name}----buffering")

            }

            override fun playing(mediaPlayer: MediaPlayer?) {
                super.playing(mediaPlayer)
                println("----${mCurSongBean?.name}----playing")
            }

            override fun paused(mediaPlayer: MediaPlayer?) {
                super.paused(mediaPlayer)
                println("----${mCurSongBean?.name}----paused")
            }

            override fun stopped(mediaPlayer: MediaPlayer?) {
                super.stopped(mediaPlayer)
                println("----${mCurSongBean?.name}----stopped")
            }
        })
    }

    private fun innerStartPlay() {
        println("----${mCurSongBean?.name}----innerStartPlay()")
        mMediaPlayer.controls().start()
        setStatus(PlayerStatus.STARTED)

    }

    override fun setDataSource(songBean: SongBean) {
        mCurSongBean = songBean
        println("----${mCurSongBean?.name}----setDataSource()")
    }

    override fun start() {
        println("----${mCurSongBean?.name}----start()")
        if (mStatus == PlayerStatus.STARTED
        ) {
            pause()
        }
        mCurSongBean?.let {
            mDuring = it.dt
            mCurTime = 0
            updateProgress()
            getSongUrlAndPlay(it.id)
        }
    }

    private fun getSongUrlAndPlay(songId: Long) {
        mJob?.cancel()
        mJob = GlobalScope.launch(context = Dispatchers.IO) {
            try {
                val url = NCRetrofitClient.getNCApi().getSongUrl(songId).data.firstOrNull()?.url
                    ?: "https://music.163.com/song/media/outer/url?id=$songId.mp3"
                mMediaPlayer.media().play(url)
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

    fun updateProgress() {
        if (mDuring != 0) {
            mListeners.forEach {
                it.onProgress(mDuring, mCurTime, mCurTime.toFloat() * 100 / mDuring)
            }
        }
    }


    override fun pause() {
        if (mStatus == PlayerStatus.STARTED) {
            println("----${mCurSongBean?.name}----pause()")
            mMediaPlayer.controls().pause()
            setStatus(PlayerStatus.PAUSED)
        }
    }

    override fun resume() {
        println("----${mCurSongBean?.name}----resume()")
        innerStartPlay()
    }

    override fun stop() {
        println("----${mCurSongBean?.name}----stop()")
        mMediaPlayer.controls().stop()
        mDuring = 0
        setStatus(PlayerStatus.STOPPED)
        setStatus(PlayerStatus.IDLE)
    }

    override fun seekTo(position: Float) {
        println("----${mCurSongBean?.name}----seekTo->${position}")
        mMediaPlayer.controls().setPosition(position / 100)
    }

    private fun setStatus(status: PlayerStatus) {
        mStatus = status
        mListeners.forEach {
            it.onStatusChanged(mStatus)
        }
    }

    override fun addListener(listener: IPlayerListener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: IPlayerListener) {
        mListeners.remove(listener)
    }
}