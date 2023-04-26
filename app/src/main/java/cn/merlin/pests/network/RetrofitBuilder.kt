package cn.merlin.pests.network

import cn.merlin.pests.network.service.ImagePostService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.TrustAnchor
import java.security.cert.X509Certificate
import javax.net.ssl.*

object RetrofitBuilder {
    private const val BASE_URL = "http://82.156.245.30:8088"

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service: ImagePostService = retrofit.create(ImagePostService::class.java)
    }
