package http

import http.api.NCApi
import http.interceptor.CookieIntercept
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object NCRetrofitClient {
    private var ncApi: NCApi? = null
    fun getNCApi(): NCApi {
        if (ncApi == null) {
            ncApi = RetrofitClient.getApi(NCApi::class.java)
        }
        return ncApi!!
    }
}

object RetrofitClient {
    const val BASE_URL = "https://ncmusic.sskevan.cn"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L
    fun <T> getApi(retrofit: Class<T>): T = createRetrofit().create(retrofit)

    private fun createRetrofit(url: String = BASE_URL): Retrofit {
        // okHttpClientBuilder
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(CookieIntercept())
        }

        return RetrofitBuild(
            url = url,
            client = okHttpClientBuilder.build(),
            gsonFactory = GsonConverterFactory.create()
        ).retrofit
    }

}

class RetrofitBuild(
    url: String, client: OkHttpClient,
    gsonFactory: GsonConverterFactory
) {
    val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonFactory)

    }.build()
}