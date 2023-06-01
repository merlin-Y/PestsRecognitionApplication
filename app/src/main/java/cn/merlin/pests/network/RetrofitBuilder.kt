package cn.merlin.pests.network

import cn.merlin.pests.network.service.ImagePostService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.TrustAnchor
import java.security.cert.X509Certificate
import java.util.Base64
import javax.net.ssl.*

object RetrofitBuilder {
    var BASE_URL = "http://82.156.245.30:8088"

    private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    var service: ImagePostService = retrofit.create(ImagePostService::class.java)
    fun setURL(URL : String){
        BASE_URL = "http://$URL"
        retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ImagePostService::class.java)
    }
}
