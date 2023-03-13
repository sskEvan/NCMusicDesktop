package model

import androidx.annotation.Keep

@Keep
data class SongDetailResult(val songs: List<SongBean>) : BaseResult()

@Keep
data class SongBean(
    //歌曲id
    val id: Long,
    //歌曲名称
    val name: String,
    val al: Al,
    val ar: List<Ar>,
    val dt: Int
) {
    fun getSongTimeLength() : String {
        val dtSecond = dt / 1000
        val min = dtSecond  / 60
        val second = dtSecond - min * 60
        val minStr = if (min < 10) "0$min" else min.toString()
        val secondStr = if (second < 10) "0$second" else second.toString()
        return "$minStr:$secondStr"
    }
}

@Keep
data class SongUrlBean(val data: List<SongUrl>)

@Keep
data class SongUrl(val url: String)