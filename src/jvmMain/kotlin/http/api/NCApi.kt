package http.api

import model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

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
    suspend fun getPlayList(
        @Query("limit") limit: Int = 20,
        @Query("tag") tag: String,
        @Query("offset") offset: Int
    ): PlayListResult

    /**
     * 获取歌单详情
     */
    @GET("playlist/detail")
    suspend fun getPlaylistDetail(@Query("id") id: Long): PlaylistDetailResult

    /**
     * 获取歌曲详情
     */
    @GET("song/detail")
    suspend fun getSongDetail(@Query("ids") ids: String): SongDetailResult

    /**
     * 获取评论列表
     */
    @GET("comment/{commentType}")
    suspend fun getCommentList(
        @Path("commentType") commentType: String,
        @Query("id") id: Long,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int,
//        @Query("before") before: Long, // 分页参数,取上一页最后一项的 time 获取下一页数据(获取超过 5000 条评论的时候需要用到)
    ): CommentResult


    /**
     * 获取二维码登录key
     */
    @GET("/login/qr/key")
    suspend fun getLoginQrcodeKey(@Query("timeStamp") timeStamp: Long = Date().time): QrcodeKeyResult

    /**
     * 获取二维码登录链接
     */
    @GET("/login/qr/create")
    suspend fun getLoginQrcodeValue(
        @Query("key") key: String,
        @Query("timeStamp") timeStamp: Long = Date().time
    ): QrcodeValueResult

    /**
     * 验证二维码登录授权结果
     */
    @GET("/login/qr/check")
    suspend fun checkQrcodeAuthStatus(
        @Query("key") key: String,
        @Query("timeStamp") timeStamp: Long = Date().time
    ): QrcodeAuthResult

    /**
     * 获取用户信息
     */
    @GET("/user/account")
    suspend fun getAccountInfo(
        @Query("cookie") cookie: String,
    ): AccountInfoResult

    /**
     * 获取用户歌单
     */
    @GET("user/playlist")
    suspend fun getUserPlayList(@Query("uid") uid: String): UserPlaylistResult

    /**
     * 获取歌曲url
     */
    @GET("/song/url")
    suspend fun getSongUrl(
        @Query("id") id: Long,
        @Query("br") br: Int = 128000
    ): SongUrlBean

    /**
     * 获取歌词
     */
    @GET("/lyric")
    suspend fun getLyric(@Query("id") id: Long): LyricResult


}