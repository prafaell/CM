package com.example.cm.API


import retrofit2.Call
import retrofit2.http.*


interface EndPoints {
    @GET("myslim/api/problemas")
    fun getReports(): Call<List<Problema>>

    @FormUrlEncoded
    @POST("myslim/api/login")
    fun getLogin(@Field("user") first: String?,@Field("pw") second: String?): Call<User>

    @FormUrlEncoded
    @POST("myslim/api/distancia")
    fun getReportsPorDistancia(@Field("lat") first: String?,@Field("lon") second: String?,@Field("raio") third: Float?):  Call<List<Problema>>

}