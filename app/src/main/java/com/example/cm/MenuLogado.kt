package com.example.cm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.cm.entities.Notas

class MenuLogado : AppCompatActivity() {

    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_logado)

        val button = findViewById<Button>(R.id.buttonOccor)

        button.setOnClickListener {
            val intent = Intent(this@MenuLogado, CriarOccor::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val titulo = data?.getStringExtra(criarnota.EXTRA_REPLY_NOME)
            val descricacao= data?.getStringExtra(criarnota.EXTRA_REPLY_DESCRICAO)


        } else {
        }
    }

    fun myNotes(view: View) {
        val intent = Intent(this,ListaNotas::class.java)
        startActivity(intent)
    }

    fun logout(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun vermapa(view: View) {
        val intent = Intent(this,MapsActivity::class.java)
        startActivity(intent)
    }


}