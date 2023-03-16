package cn.merlin.pests.network

import cn.merlin.pests.network.service.ImagePostService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://192.168.0.194:5000"

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service: ImagePostService = retrofit.create(ImagePostService::class.java)
    }
