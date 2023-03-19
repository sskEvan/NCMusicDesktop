package base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import base.player.*
import model.SongBean
import util.StringUtil
import java.util.*

object MusicPlayController : IPlayerListener {
    // 是否显示音乐播放抽屉组件
    var showMusicPlayDrawer by mutableStateOf(false)
    // 是否显示播当前播放列表
    var showCurPlayListSheet by mutableStateOf(false)

    // 原始歌曲列表
    var originSongList = mutableStateListOf<SongBean>()
    // 当前播放模式下的实际歌曲列表
    var realSongList = mutableStateListOf<SongBean>()

    var curSongBean by mutableStateOf<SongBean?>(null)
    // 当前播放的歌曲在原始歌曲列表中的索引
    var curOriginIndex by mutableStateOf(-1)
        private set
    // 当前播放的歌曲在当前播放模式下的实际歌曲列表中的索引
    var curRealIndex by mutableStateOf(-1)
        private set
    // 当前播放进度
    var progress by mutableStateOf(0f)
    // 当前歌曲播放位置时间文本
    var curPositionStr by mutableStateOf("00:00")
    // 当前歌曲总时长文本
    var totalDuringStr by mutableStateOf("00:00")
    // 是否播放中
    private var playing by mutableStateOf(false)
    // 是否允许拖动进度条
    var enableSeeking by mutableStateOf(false)
        private set
    // 播放模式
    var playMode by mutableStateOf(PlayMode.LOOP)
        private set
    // 当前播放歌曲总时长
    private var totalDuring = 0
    // 是否拖动进度条中
    private var seeking = false
    // 当前播放状态
    private var playerStatus: PlayerStatus = PlayerStatus.IDLE

    val mediaPlayer: IPlayer by lazy { NCPlayer().apply {
        addListener(this@MusicPlayController)
    } }


    /**
     * 播放音乐列表
     */
    fun playMusicList(songBeans: List<SongBean>, originIndex: Int) {
        originSongList.clear()
        originSongList.addAll(songBeans)
        println("MusicPlayController playMusicList curOriginIndex=${originIndex}")
        generateRealSongList(originIndex)
        innerPlay(originSongList[originIndex])
    }


    /**
     * 生成当前播放模式下的歌曲列表
     */
    private fun generateRealSongList(originIndex: Int) {
        when (playMode) {
            PlayMode.RANDOM -> {
                val randomList = mutableListOf<SongBean>()
                randomList.addAll(originSongList)
                randomList.shuffle()
                realSongList.clear()
                realSongList.addAll(randomList)
                val realIndex = realSongList.indexOfFirst { it.id == originSongList[originIndex].id }
                if (realIndex != originIndex) {
                    Collections.swap(realSongList, realIndex, originIndex)
                }
                curOriginIndex = originIndex
                curRealIndex = originIndex
            }
            else -> {
                realSongList.clear()
                realSongList.addAll(originSongList)
                curOriginIndex = originIndex
                curRealIndex = originIndex
            }
        }
//        originSongList.forEachIndexed { index, item ->
//            println("songList $index --> ${item.name}")
//        }
//        println("---------------------------------------")
//
//        realSongList.forEachIndexed { index, item ->
//            println("pagerSongList $index --> ${item.name}")
//        }
    }

    private fun innerPlay(songBean: SongBean) {
        curSongBean = songBean
        mediaPlayer.setDataSource(songBean)
        mediaPlayer.start()
    }

    /**
     * 根据原始歌曲列表索引播放音乐
     */
//    fun playByOriginIndex(originIndex: Int) {
//        if (originSongList.size > originIndex) {
//            curOriginIndex = originIndex
//            curRealIndex = realSongList.indexOfFirst { it.id == originSongList[originIndex].id }
//            innerPlay(originSongList[curOriginIndex])
//        }
//    }

    /**
     * 根据实际播放模式中的歌曲列表索引播放音乐
     */
    fun playByRealIndex(realIndex: Int) {
        if (originSongList.getOrNull(realIndex) != null) {
            curRealIndex = realIndex
            curOriginIndex = originSongList.indexOfFirst { it.id == realSongList[realIndex].id }
            innerPlay(realSongList[curRealIndex])
        }
    }

    override fun onStatusChanged(status: PlayerStatus) {
        playerStatus = status
        playing = status == PlayerStatus.STARTED
        enableSeeking = status == PlayerStatus.STARTED || status == PlayerStatus.PAUSED
        when (status) {
            PlayerStatus.COMPLETED -> {
                autoPlayNext()
            }
            PlayerStatus.ERROR -> {
                autoPlayNext()
            }
            PlayerStatus.STOPPED -> {
                totalDuringStr = "00:00"
                curPositionStr = "00:00"
                this.progress = 0f
            }
            else -> {}
        }
    }

    private fun autoPlayNext() {
        if(playMode == PlayMode.SINGLE) {
            resume()
        }else {
            val newIndex = getNextRealIndex()
            playByRealIndex(newIndex)
        }
    }

    fun pause() {
        if (playerStatus == PlayerStatus.STARTED) {
            mediaPlayer.pause()
        }
    }

    fun resume() {
        if (playerStatus == PlayerStatus.PAUSED) {
            mediaPlayer.resume()
        }
    }

    fun isPlaying(): Boolean {
        return playing
    }

    /**
     * 获取当前播放模式下的上一首歌曲索引
     */
    fun getPreRealIndex() = if (curRealIndex == 0) realSongList.size - 1 else curRealIndex - 1

    /**
     * 获取当前播放模式下的下一首歌曲索引
     */
    fun getNextRealIndex() = if (curRealIndex == realSongList.size - 1) 0 else curRealIndex + 1

    fun changePlayMode(playMode: PlayMode) {
        this.playMode = playMode
        generateRealSongList(curOriginIndex)
    }

    fun seekTo(progress: Float) {
        this.progress = progress
        if (totalDuring != 0) {
            mediaPlayer.seekTo(progress)
        }
        seeking = false
    }

    fun seeking(progress: Float) {
        seeking = true
        this.progress = progress
        if (totalDuring != 0) {
            this.curPositionStr = StringUtil.formatMilliseconds((progress * totalDuring / 100).toInt())
        }
    }

    override fun onProgress(totalDuring: Int, currentPosition: Int, percentage: Float) {
        if (!seeking) {
            this.totalDuring = totalDuring
            totalDuringStr = StringUtil.formatMilliseconds(totalDuring)
            curPositionStr = StringUtil.formatMilliseconds(currentPosition)
            progress = percentage
        }
    }


}
