package http.api

import model.RecommendPlayListBean
import retrofit2.http.GET
import retrofit2.http.Query

interface NCApi {

    /**
     * 获取推荐歌单
     */
    @GET("personalized")
    suspend fun getRecommendPlayList(@Query("limit") limit: Int = 20): RecommendPlayListBean

}