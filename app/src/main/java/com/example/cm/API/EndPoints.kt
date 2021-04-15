package com.example.cm.API


import retrofit2.Call
import retrofit2.http.*


interface EndPoints {
    @GET("/api/problemas")
    fun getReports(): Call<List<Problema>>

    @FormUrlEncoded
    @POST("/api/login")
    fun getLogin(@Field("user") first: String?,@Field("pw") second: String?): Call<User>
}