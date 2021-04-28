package com.example.cm.API

data class User(
        val id: Int,
        val user: String,
        val pw: String,
        val status: Boolean,
        val MSG: String
)

data class Problema(
        val id: Int?,
        val titulo: String?,
        val descricao: String?,
        var lat: String?,
        var lon: String?,
        var imagem: String?,
        var utilizador_id: Int?,
        var tipo: String?,
        var MSG: String,
        val status: Boolean
)