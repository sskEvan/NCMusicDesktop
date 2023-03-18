package model


/**
 * 独家放送
 */
data class PrivateContentResult(
    val result: List<PrivateContentItem>
) : BaseResult() {
    override fun isEmpty() = result.isEmpty()
}

data class PrivateContentItem(
    val id: Long,
    val url: String?,
    val picUrl: String,
    val sPicUrl: String,
    val type: Int,
    val copywriter: String,
    val name: String,
    val alg: String
)