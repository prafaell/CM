package com.example.cm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cm.API.EndPoints
import com.example.cm.API.Problema
import com.example.cm.API.ServiceBuilder
import com.example.cm.MapsActivity.Companion.EXTRA_REPLY_ID
import com.example.cm.adapter.DESC
import com.example.cm.adapter.TITULO
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OcorrAlterar : AppCompatActivity() {

    private lateinit var ocorrtitulo: EditText
    private lateinit var ocorrdesc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorr_alterar)

        val id = intent.getStringExtra(EXTRA_REPLY_ID)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProblemaPorID(id)

        call.enqueue(object : Callback<Problema> {
            override fun onResponse(call: Call<Problema>, response: Response<Problema>) {
                if (response.isSuccessful){

                    val problema = response.body()!!

                    findViewById<EditText>(R.id.tituloNovo).setText(problema.titulo)
                    findViewById<EditText>(R.id.descrNovo).setText(problema.descricao)
                    findViewById<TextView>(R.id.tipoNovo).setText(problema.tipo)

                    val imageview = findViewById<ImageView>(R.id.imagem)

                    Log.d("CARALHO",problema.imagem)

                    if(!problema.imagem.isNullOrEmpty()){
                        Picasso.get().load("https://smartcitycm2021.000webhostapp.com/myslim/api/imagens/" + problema.imagem + ".png").into(imageview);
                    }


                }
            }
            override fun onFailure(call: Call<Problema>, t: Throwable) {
                Toast.makeText(this@OcorrAlterar, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}