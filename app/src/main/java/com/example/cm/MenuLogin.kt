package com.example.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.cm.API.EndPoints
import com.example.cm.API.Problema
import com.example.cm.API.ServiceBuilder
import com.example.cm.API.User
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_login)


    }

    fun logar(view: View) {
        val intent = Intent(this,MenuLogado::class.java)

        val user = findViewById<EditText>(R.id.editTextTextPersonName).text.toString();
        val pw = findViewById<EditText>(R.id.editTextTextPassword).text.toString();

        Log.d("USER",user);
        Log.d("pw",pw)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getLogin(user,pw);
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@MenuLogin, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })


    }



}