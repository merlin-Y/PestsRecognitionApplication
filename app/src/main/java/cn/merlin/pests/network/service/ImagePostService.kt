package cn.merlin.pests.network.service

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImagePostService {

    @Multipart
    @POST("/image_post")
    fun imagePost(@Part file : MultipartBody.Part): Call<ResponseBody>

}