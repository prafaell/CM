package com.example.cm.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Nota_tabela")

class Notas(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "descricao") val descricao: String
)
