package com.example.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MenuLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_login)
    }

    fun logar(view: View) {
        val intent = Intent(this,MenuLogado::class.java)
        startActivity(intent)
    }
}