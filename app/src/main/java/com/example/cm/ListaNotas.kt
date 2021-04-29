package com.example.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cm.adapter.LineAdapter
import com.example.cm.entities.Notas
import com.example.cm.viewmodel.NotaViewModel


class ListaNotas : AppCompatActivity(),LineAdapter.CallbackInterface {


    private lateinit var notaViewModel: NotaViewModel
    private val newWordActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = LineAdapter(this,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        val button = findViewById<Button>(R.id.butaoNovo)

        button.setOnClickListener {
            val intent = Intent(this@ListaNotas, criarnota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

     override fun passResultCallback(message: Int?) {
        notaViewModel.deleteByID(message)
        Toast.makeText(this,getString(R.string.apagado), LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val pnome = data?.getStringExtra(criarnota.EXTRA_REPLY_NOME)
            val pdescricao= data?.getStringExtra(criarnota.EXTRA_REPLY_DESCRICAO)

            if (pnome!= null && pdescricao != null) {
                val nota = Notas(nome = pnome, descricao = pdescricao)
                notaViewModel.insert(nota)
            }

        } else {
        }
    }
}