package model

import com.ssk.ncmusic.model.BaseResult

/**
 * 推荐MV
 */
data class RecommendMVResult(
val result: List<RecommendMVItem>
): BaseResult()

data class RecommendMVItem(
    val id: Long,
    val name: String,
    val artistName: String,
    val copywriter: String,
    val picUrl: String,
    val playCount: Long
)