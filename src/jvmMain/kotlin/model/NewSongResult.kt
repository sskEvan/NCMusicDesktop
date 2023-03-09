package model

import com.ssk.ncmusic.model.BaseResult

/**
 * 新歌速递
 */
data class NewSongResult(
    var data: List<NewSongBean>
) : BaseResult()

data class NewSongBean(
    val name: String,
    val artists: List<ArtistBean>,
    val album: Album
)

data class ArtistBean(
    val id: Long,
    val name: String
)

data class Album(
    val id: Long,
    val name: String,
    val picUrl: String
)