package com.example.cm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cm.db.NotasRepository
import com.example.cm.db.NotasDB
import com.example.cm.entities.Notas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotasRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotas: LiveData<List<Notas>>

    init {
        val notaDao = NotasDB.getDatabase(application, viewModelScope).NotaDao()
        repository = NotasRepository(notaDao)
        allNotas = repository.allNotas
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(nota: Notas) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }


    // delete by nome
    fun deleteByNome(nota: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByNome(nota)
    }

    fun update(nota: Notas) = viewModelScope.launch {
        repository.update(nota)
    }


}