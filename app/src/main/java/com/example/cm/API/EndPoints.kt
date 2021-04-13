package com.example.cm.API


import retrofit2.Call
import retrofit2.http.*


interface EndPoints {
    @GET("/api/problemas")
    fun getReports(): Call<List<Problema>>

    @GET("/api/utilizadores")
    fun getUsers(): Call<List<User>>
}