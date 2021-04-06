package com.example.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.recyclerline.*

class criarnota : AppCompatActivity() {

    private lateinit var nome: EditText
    private lateinit var descricao: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criarnota)


        nome = findViewById(R.id.editTextTextPersonName3)
        descricao = findViewById(R.id.editTextTextMultiLine)

        val button = findViewById<Button>(R.id.button6)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(nome.text)) {
                Toast.makeText(this,"Titulo necessario", Toast.LENGTH_LONG).show()
            } else {
                replyIntent.putExtra(EXTRA_REPLY_NOME, nome.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricao.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                Toast.makeText(this,"Criado com sucesso", Toast.LENGTH_LONG).show()
                finish()
            }

        }

    }
    companion object {
        const val EXTRA_REPLY_NOME = "nome"
        const val EXTRA_REPLY_DESCRICAO = "descricao"
    }

}