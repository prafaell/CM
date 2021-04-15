package com.example.cm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cm.API.EndPoints
import com.example.cm.API.Problema
import com.example.cm.API.ServiceBuilder
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
        startActivity(intent)
    }

    val request = ServiceBuilder.buildService(EndPoints::class.java)
    val call = request.getLogin()


    call.enqueue(object : Callback<List<Problema>> {
        override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
            if (response.isSuccessful){
                problemas = response.body()!!
                for (problema in problemas){
                    position = LatLng(problema.lat.toString().toDouble(), problema.lon.toString().toDouble())
                    mMap.addMarker(MarkerOptions().position(position).title(problema.titulo))
                }

            }
        }
        override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
            Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_LONG).show()
        }
    })

}