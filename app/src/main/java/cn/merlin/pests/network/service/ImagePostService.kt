package cn.merlin.pests.network.service

import cn.merlin.pests.network.model.PestModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImagePostService {

    @GET("/imagepost")
    fun imagePost(): Call<String>

}