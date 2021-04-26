package com.example.cm.API


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface EndPoints {
    @GET("myslim/api/problemas")
    fun getReports(): Call<List<Problema>>

    @FormUrlEncoded
    @POST("myslim/api/login")
    fun getLogin(@Field("user") first: String?,@Field("pw") second: String?): Call<User>

    @Multipart
    @POST("/myslim/api/inserir")
    fun inserirOcorr(
        @Part("titulo") titulo: RequestBody,
        @Part("descricao") descricao: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part imagem: MultipartBody.Part,
        @Part("utilizador") utilizador: RequestBody,
        @Part("tipo") tipo: RequestBody

    ): Call<Problema>
}