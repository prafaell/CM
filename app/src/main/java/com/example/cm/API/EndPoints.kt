package com.example.cm.API


import retrofit2.Call
import retrofit2.http.*


interface EndPoints {
    @GET("myslim/api/problemas")
    fun getReports(): Call<List<Problema>>

}