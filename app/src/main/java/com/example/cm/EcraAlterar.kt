package com.example.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cm.adapter.DESC
import com.example.cm.adapter.ID
import com.example.cm.adapter.TITULO
import com.example.cm.entities.Notas
import com.example.cm.viewmodel.NotaViewModel
import kotlinx.android.synthetic.main.recyclerline.*

class EcraAlterar : AppCompatActivity() {
    private lateinit var notatitulo: EditText
    private lateinit var notadesc: EditText
    private lateinit var notaViewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_alterar)

        val nome = intent.getStringExtra(TITULO)
        val desc = intent.getStringExtra(DESC)


        findViewById<EditText>(R.id.nomenovo).setText(nome)
        findViewById<EditText>(R.id.descnovo).setText(desc)

        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)

    }

    fun editarNota(view: View) {
        notatitulo = findViewById(R.id.nomenovo)
        notadesc = findViewById(R.id.descnovo)
        var message3 = intent.getIntExtra(ID, 0)
        val replyIntent = Intent()
        if (TextUtils.isEmpty(notatitulo.text))  {
            Toast.makeText(this,"Titulo vazio", Toast.LENGTH_LONG).show()
        } else {
            val nota = Notas(id = message3, nome = notatitulo.text.toString(), descricao = notadesc.text.toString() )
            notaViewModel.update(nota)
            Toast.makeText(this,"Alterado com sucesso", Toast.LENGTH_LONG).show()
            finish()
        }


    }
}