package model

/**
 * 推荐MV
 */
data class RecommendMVResult(
    val result: List<RecommendMVItem>
) : BaseResult() {
    override fun isEmpty() = result.isEmpty()
}

data class RecommendMVItem(
    val id: Long,
    val name: String,
    val artistName: String,
    val copywriter: String,
    val picUrl: String,
    val playCount: Long
)