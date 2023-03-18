package base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import base.player.NCPlayer
import base.player.PlayMode
import model.SongBean
import java.util.*

object MusicPlayController {
    // 是否显示音乐播放抽屉组件
    var showMusicPlayDrawer by mutableStateOf(false)

    var showPlayListSheet by mutableStateOf(false)

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
    var progress by mutableStateOf(0)
    // 当前歌曲播放位置时间文本
    var curPositionStr by mutableStateOf("00:00")
    // 当前歌曲总时长文本
    var totalDuringStr by mutableStateOf("00:00")
    // 是否播放中
    private var playing by mutableStateOf(false)
    // 播放模式
    var playMode by mutableStateOf(PlayMode.LOOP)
        private set
    // 当前播放歌曲总时长
    private var totalDuring = 0
    // 是否拖动进度条中
    private var seeking = false


    fun setDataSource(songBeans: List<SongBean>, originIndex: Int) {
        originSongList.clear()
        originSongList.addAll(songBeans)
        println("MusicPlayController setDataSource curOriginIndex=${originIndex}")
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
        originSongList.forEachIndexed { index, item ->
            println("songList $index --> ${item.name}")
        }
        println("---------------------------------------")

        realSongList.forEachIndexed { index, item ->
            println("pagerSongList $index --> ${item.name}")
        }
    }

    private fun innerPlay(songBean: SongBean) {
        curSongBean = songBean
        NCPlayer.setDataSource(songBean)
        NCPlayer.start()
    }

    /**
     * 根据原始歌曲列表索引播放音乐
     */
    fun playByOriginIndex(originIndex: Int) {
        if (originSongList.size > originIndex) {
            curOriginIndex = originIndex
            curRealIndex = realSongList.indexOfFirst { it.id == originSongList[originIndex].id }
            innerPlay(originSongList[curOriginIndex])
        }
    }

    /**
     * 根据实际播放模式中的歌曲列表索引播放音乐
     */
    fun playByRealIndex(realIndex: Int) {
        if (originSongList.size > realIndex) {
            curRealIndex = realIndex
            curOriginIndex = originSongList.indexOfFirst { it.id == realSongList[realIndex].id }
            innerPlay(realSongList[curRealIndex])
        }
    }


}
