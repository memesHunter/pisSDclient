package ru.pis.sdclient

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(90, TimeUnit.SECONDS)
    .readTimeout(90, TimeUnit.SECONDS)
    .writeTimeout(90, TimeUnit.SECONDS)
    .build()
lateinit var retrofit: Retrofit
lateinit var api: SDRetrofitAPI

data class Txt2ImgPayload(
    val prompt: String,
    val negative_prompt: String,

    val seed: Int = -1,
    val steps: Int = 20,
    val batch_size: Int = 1,
    val sampler_name: String = "Euler a",

    val width: Int = 512,
    val height: Int = 512,
)

data class Img2ImgPayload(
    val init_images: List<String>,
    val prompt: String,
    val negative_prompt: String,

    val seed: Int = -1,
    val steps: Int = 20,
    val denoising_strength: Double = 0.75,
    val batch_size: Int = 1,
    val sampler_name: String = "Euler a",

    val width: Int = 512,
    val height: Int = 512,
)

interface SDRetrofitAPI {
    @POST("sdapi/v1/txt2img")
    fun createImage(@Body data: Txt2ImgPayload): Call<ResponseBody>

    @POST("sdapi/v1/img2img")
    fun createImageFromImage(@Body data: Img2ImgPayload): Call<ResponseBody>

    @GET("internal/ping")
    fun checkConnection(): Call<ResponseBody>

    @GET("sdapi/v1/progress")
    fun getProgress(): Call<ResponseBody>
}