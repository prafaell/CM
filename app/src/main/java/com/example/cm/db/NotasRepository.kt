package com.example.cm.db

import androidx.lifecycle.LiveData
import com.example.cm.dao.NotaDao
import com.example.cm.entities.Notas

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NotasRepository(private val notaDao: NotaDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotas: LiveData<List<Notas>> = notaDao.getTodasNotas()

    suspend fun insert(nota: Notas) {
        notaDao.insert(nota)
    }


    suspend fun deleteByID(id: Int?){
        notaDao.deleteByID(id)
    }


    suspend fun update(nota: Notas) {
        notaDao.update(nota)
    }
}