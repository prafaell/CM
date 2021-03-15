package com.example.cm.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cm.entities.Notas

@Dao
interface NotaDao {
    @Query("select * from Nota_tabela order by nome ASC")
    fun getTodasNotas(): LiveData<List<Notas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Notas)

    @Update
    suspend fun update(nota: Notas)

    @Query("DELETE FROM Nota_tabela where id == :id")
    suspend fun deleteByID(id: Int?)
}