package http.api

import model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface NCApi {

    /**
     * 获取推荐歌单
     */
    @GET("personalized")
    suspend fun getRecommendPlayList(@Query("limit") limit: Int = 20): RecommendPlayListResult

    /**
     * 获取独家放送
     */
    @GET("personalized/privatecontent")
    suspend fun getPrivateContent(): PrivateContentResult

    /**
     * 新歌速递
     */
    @GET("/top/song")
    suspend fun getNewSong(): NewSongResult

    /**
     * 新歌速递
     */
    @GET("/personalized/mv")
    suspend fun getRecommendMV(): RecommendMVResult

    /**
     * 精品歌单
     */
    @GET("/top/playlist/highquality")
    suspend fun getHighQualityPlayList(@Query("limit") limit: Int = 20, @Query("cat") cat: String?): PlayListResult

    /**
     * 热门歌单分类
     */
    @GET("/playlist/hot")
    suspend fun getHotPlayListCategories(): HotPlayListTabResult


    /**
     * 歌单分类
     */
    @GET("/playlist/catlist")
    suspend fun getPlayListCategories(): PlayListTabResult


    /**
     * 歌单列表
     */
    @GET("/top/playlist")
    suspend fun getPlayList(@Query("limit") limit: Int = 20): PlayListResult

}