package com.example.cm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_ocorr_alterar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OcorrAlterar : AppCompatActivity() {

    private lateinit var ocorrtitulo: EditText
    private lateinit var ocorrdesc: EditText
    private  var idUserOcorr: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorr_alterar)

        val id = intent.getStringExtra(EXTRA_REPLY_ID)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 0);



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

                    if(!problema.imagem.isNullOrEmpty()){
                        Picasso.get().load("https://smartcitycm2021.000webhostapp.com/myslim/api/imagens/" + problema.imagem + ".png").into(imageview);
                    }

                    idUserOcorr = problema.utilizador_id
                }
            }
            override fun onFailure(call: Call<Problema>, t: Throwable) {
                Toast.makeText(this@OcorrAlterar, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })


        btnEliminar.setOnClickListener{
            val call = request.getEliminarPorID(id)

            if(userid == idUserOcorr){
                call.enqueue(object : Callback<Problema> {
                    override fun onResponse(call: Call<Problema>, response: Response<Problema>) {
                        if (response.isSuccessful){
                            if(response.body()!!.status){
                                Toast.makeText(this@OcorrAlterar,getString(R.string.apagado), Toast.LENGTH_LONG).show()

                                val intent = Intent(this@OcorrAlterar, MapsActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)

                                finish()
                            }else{
                                Toast.makeText(this@OcorrAlterar,getString(R.string.erro), Toast.LENGTH_LONG).show()
                            }


                        }
                    }
                    override fun onFailure(call: Call<Problema>, t: Throwable) {
                        Toast.makeText(this@OcorrAlterar, "${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }else{
                Toast.makeText(this@OcorrAlterar,this.getString(R.string.permissao), Toast.LENGTH_LONG).show()
            }
        }


        btnGuardar.setOnClickListener{
            if(userid == idUserOcorr){
                ocorrtitulo = findViewById(R.id.tituloNovo)
                ocorrdesc = findViewById(R.id.descrNovo)

                if(ocorrtitulo.text.toString().isNullOrEmpty() || ocorrdesc.text.toString().isNullOrEmpty()){
                    Toast.makeText(this@OcorrAlterar,getString(R.string.camposvazios), Toast.LENGTH_LONG).show()
                }else{
                    val call = request.getAlterarPorID(id,ocorrtitulo.text.toString(),ocorrdesc.text.toString())

                    call.enqueue(object : Callback<Problema> {
                        override fun onResponse(call: Call<Problema>, response: Response<Problema>) {
                            if (response.isSuccessful){
                                if(response.body()!!.status){
                                    Toast.makeText(this@OcorrAlterar,getString(R.string.alterado), Toast.LENGTH_LONG).show()

                                    val intent = Intent(this@OcorrAlterar, MapsActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)

                                    finish()
                                }else{
                                    Toast.makeText(this@OcorrAlterar, getString(R.string.erro), Toast.LENGTH_LONG).show()
                                }


                            }
                        }
                        override fun onFailure(call: Call<Problema>, t: Throwable) {
                            Toast.makeText(this@OcorrAlterar, "${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                }

            }else{
                Toast.makeText(this@OcorrAlterar,this.getString(R.string.permissao), Toast.LENGTH_LONG).show()
            }
        }
    }
}