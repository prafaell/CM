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

    @FormUrlEncoded
    @POST("myslim/api/tipo")
    fun getProblemasPorTipo(@Field("tipo") first: String?): Call<List<Problema>>


    @GET("myslim/api/problemaPorID/{id}")
    fun getProblemaPorID(@Path("id") id: String): Call<Problema>

    @GET("myslim/api/eliminar/{id}")
    fun getEliminarPorID(@Path("id") id: String): Call<Problema>

    @FormUrlEncoded
    @POST("myslim/api/alterar")
    fun getAlterarPorID(@Field("id") first: String,@Field("titulo") second: String?,@Field("descricao") third: String?): Call<Problema>
}