package com.example.cm

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_criar_occor.*

class CriarOccor : AppCompatActivity() {

    private lateinit var nome: EditText
    private lateinit var descricao: EditText
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_occor)


        nome = findViewById(R.id.titulo)
        descricao = findViewById(R.id.descr)

        btncamera.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }

        }

        val button = findViewById<Button>(R.id.button7)
        button.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), CriarOccor.LOCATION_PERMISSION_REQUEST_CODE)

            }else{
                mMap.isMyLocationEnabled = true

                fusedLocationClient.lastLocation.addOnSuccessListener(this){
                    location ->
                    if(location != null){
                        lastLocation = location;

                        val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                        val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 0);

                        val replyIntent = Intent()
                        if (TextUtils.isEmpty(nome.text)) {
                            Toast.makeText(this,"Titulo necessario", Toast.LENGTH_LONG).show()
                        } else {
                            /*
                            replyIntent.putExtra(EXTRA_REPLY_NOME, nome.text.toString())
                            replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricao.text.toString())
                            replyIntent.putExtra(EXTRA_REPLY_LAT, location.latitude.toString())
                            replyIntent.putExtra(EXTRA_REPLY_LON, location.longitude.toString())
                            replyIntent.putExtra(EXTRA_REPLY_ID,userid)
                            setResult(Activity.RESULT_OK, replyIntent)
                            Toast.makeText(this,"Criado com sucesso", Toast.LENGTH_LONG).show()
                            */

                            finish()
                        }
                    }
                }
            }



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imagem = data?.extras?.get("data") as Bitmap
            image_view.setImageBitmap(imagem)
        }
    }


    companion object {
        const val EXTRA_REPLY_NOME = "nome"
        const val EXTRA_REPLY_DESCRICAO = "descricao"
        const val EXTRA_REPLY_LAT = "lat"
        const val EXTRA_REPLY_LON = "long"
        const val EXTRA_REPLY_ID = "id"
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }



}