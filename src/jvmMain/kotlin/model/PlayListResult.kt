package model

import androidx.annotation.Keep
import com.ssk.ncmusic.model.BaseResult
import java.io.Serializable

/**
 * 推荐歌单结果
 */
data class RecommendPlayListBean(
    val result: List<RecommendPlayListItem>
) : BaseResult()

/**
 * 推荐歌单列表item
 */
data class RecommendPlayListItem(
    val id: Long,
    val type: Int,
    val name: String,
    val picUrl: String,
    val trackNumberUpdateTime: Long,
    val playCount: Long,
    val trackCount: Int
) : Serializable

@Keep
data class PlaylistBean(
    val tracks: List<Track>?,
    val trackIds: List<TrackId>?,
    val creator: Subscribers,
    val name: String = "",
    val coverImgUrl: String = "",
    val trackCount: Int = 0,
    val id: Long = 0,
    val playCount: Long = 0,
    val description: String?,
    val shareCount: Int,
    val commentCount: Int
) : Serializable

@Keep
data class Subscribers(
    val userId: Long, val avatarUrl: String, val nickname: String
) : Serializable

@Keep
data class Track(
    val name: String,
    val id: Long,
    val mv: Long,
    val ar: List<Ar>,
    val al: Al,
) : Serializable

@Keep
data class TrackId(
    val id: Int = 0, val v: Int = 0, val alg: String? = null
) : Serializable

@Keep
data class Ar(
    val id: Long,
    val name: String,
)

@Keep
data class Al(
    val id: Long,
    val name: String,
    val picUrl: String,
)