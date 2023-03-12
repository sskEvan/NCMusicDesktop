package model

import androidx.annotation.Keep
import com.ssk.ncmusic.model.BaseResult
import java.io.Serializable

/**
 * 推荐歌单结果
 */
data class RecommendPlayListResult(
    val result: List<SimplePlayListItem>
) : BaseResult()

/**
 * 推荐歌单列表item
 */
data class SimplePlayListItem(
    val id: Long,
    val name: String,
    val picUrl: String,
    val copywriter: String?,
    val trackNumberUpdateTime: Long,
    val playCount: Long,
    val trackCount: Int,
    val subscribedCount: Int,
    val shareCount: Int
) : Serializable

@Keep
data class PlaylistDetail(
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
    val commentCount: Int,
    val trackUpdateTime: Long,
    val subscribedCount: Int,
    val subscribers: List<Subscribers>,
    val tags: List<String>,
) : Serializable {
    fun convertToSimple()  = SimplePlayListItem(id, name, coverImgUrl, description, trackUpdateTime, playCount, trackCount, subscribedCount, shareCount)
}

/**
 * 歌单结果
 */
data class PlayListResult(
    val playlists: List<PlaylistDetail>,
    val total: Int,
    val more: Boolean
) : BaseResult(), IBasePagingBean {
    override fun getTotalCount() = total
}




@Keep
data class Subscribers(
    val userId: Long, val avatarUrl: String, val nickname: String, val description: String?
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



/**
 * 热门歌单分类
 *
 */
data class HotPlayListTabResult(val tags: List<PlayListTab>) : BaseResult()


data class PlayListTab(
    val id: Int,
    val name: String,
    val category: Int,
    val hot: Boolean,
)


/**
 * 歌单分类
 */
data class PlayListTabResult(
    val all: PlayListTab,
    val sub: List<PlayListTab>,
    val categories: Map<Int, String>) : BaseResult()

@Keep
data class PlaylistDetailResult(
    val playlist: PlaylistDetail,
) : BaseResult()


