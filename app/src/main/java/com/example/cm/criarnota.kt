package com.example.cm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
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
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_NOME, nome.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricao.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

    }
    companion object {
        const val EXTRA_REPLY_NOME = "com.example.android.nome"
        const val EXTRA_REPLY_DESCRICAO = "com.example.android.descricao"
    }

}