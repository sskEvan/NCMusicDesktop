package base.player

import http.NCRetrofitClient
import kotlinx.coroutines.*
import model.SongBean
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent

/**
 * 音乐播放器,基于vlcj
 */
class NCPlayer : IPlayer {

    private var mStatus: PlayerStatus = PlayerStatus.IDLE
    private var mCurSongBean: SongBean? = null
    private val mListeners = ArrayList<IPlayerListener>()
    private var mJob: Job? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mDuring: Int = 0
    private var mCurTime: Int = 0

    init {
        if (envAvailable()) {
            mMediaPlayer = AudioPlayerComponent().mediaPlayer().apply {
                this.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
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

                    override fun error(mediaPlayer: MediaPlayer?) {
                        super.error(mediaPlayer)
                        println("----${mCurSongBean?.name}----error")
                        setStatus(PlayerStatus.ERROR(PlayerErrorCode.ERROR_PLAY, "播放失败"))
                    }
                })
            }
        }
    }

    private fun innerStartPlay() {
        if (envAvailable()) {
            println("----${mCurSongBean?.name}----innerStartPlay()")
            mMediaPlayer?.controls()?.start()
            setStatus(PlayerStatus.STARTED)
        }
    }

    override fun setDataSource(songBean: SongBean) {
        mCurSongBean = songBean
        println("----${mCurSongBean?.name}----setDataSource()")
    }

    override fun start() {
        if (envAvailable()) {
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
    }

    private fun getSongUrlAndPlay(songId: Long) {
        mJob?.cancel()
        mJob = GlobalScope.launch(context = Dispatchers.IO) {
            try {
                val url = NCRetrofitClient.getNCApi().getSongUrl(songId).data.firstOrNull()?.url
                    ?: "https://music.163.com/song/media/outer/url?id=$songId.mp3"
                mMediaPlayer?.media()?.play(url)
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    println("getSongUrlAndPlay e = $e")
                    e.printStackTrace()
                    mListeners.forEach {
                        it.onStatusChanged(PlayerStatus.ERROR(PlayerErrorCode.ERROR_GET_URL, "获取歌曲播放链接失败"))
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
        if (envAvailable()) {
            if (mStatus == PlayerStatus.STARTED) {
                println("----${mCurSongBean?.name}----pause()")
                mMediaPlayer?.controls()?.pause()
                setStatus(PlayerStatus.PAUSED)
            }
        }
    }

    override fun resume() {
        println("----${mCurSongBean?.name}----resume()")
        innerStartPlay()
    }

    override fun stop() {
        if (envAvailable()) {
            println("----${mCurSongBean?.name}----stop()")
            mMediaPlayer?.controls()?.stop()
            mDuring = 0
            setStatus(PlayerStatus.STOPPED)
            setStatus(PlayerStatus.IDLE)
        }
    }

    override fun seekTo(position: Float) {
        if (envAvailable()) {
            println("----${mCurSongBean?.name}----seekTo->${position}")
            mMediaPlayer?.controls()?.setPosition(position / 100)
        }
    }

    private fun setStatus(status: PlayerStatus) {
        mStatus = status
        mListeners.forEach {
            it.onStatusChanged(mStatus)
        }
    }

    override fun envAvailable(): Boolean {
        if (NativeDiscovery().discover()) {
            return true
        } else {
            setStatus(PlayerStatus.ERROR(PlayerErrorCode.ERROR_ENV_INVALID, "NCMusicDesktop播放音乐需依赖VLC组件，当前设备未检测到VLC组件，请前往 https://www.videolan.org/ 下载并安装。"))
            return false
        }
    }

    override fun addListener(listener: IPlayerListener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: IPlayerListener) {
        mListeners.remove(listener)
    }
}