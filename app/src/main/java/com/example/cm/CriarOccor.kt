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
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.cm.API.EndPoints
import com.example.cm.API.Problema
import com.example.cm.API.ServiceBuilder
import com.example.cm.API.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_criar_occor.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class CriarOccor : AppCompatActivity() {

    private lateinit var nome: EditText
    private lateinit var descri: EditText

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_occor)



        val spinner: Spinner = findViewById(R.id.spinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.tipos,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        nome = findViewById(R.id.titulo)
        descri = findViewById(R.id.descr)


        val btncamera = findViewById<Button>(R.id.btncamera)
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

                        val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE)


                        val replyIntent = Intent()
                        if (TextUtils.isEmpty(nome.text)) {
                            Toast.makeText(this,getString(R.string.titulovazio), Toast.LENGTH_LONG).show()
                        } else {

                            val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                            val userid:Int? = sharedPref.getInt(R.string.userid.toString(), 0);
                            val latitude:String? = sharedPref.getString(R.string.lat.toString(), "lat")
                            val longitude:String? = sharedPref.getString(R.string.lon.toString(), "lon");

                            val request = ServiceBuilder.buildService(EndPoints::class.java)

                            val imgBitmap: Bitmap = findViewById<ImageView>(R.id.image_view).drawable.toBitmap()
                            val imgFile: File = convertBitmapToFile("file", imgBitmap)
                            val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), imgFile)
                            val imagem: MultipartBody.Part = MultipartBody.Part.createFormData("imagem", imgFile.name, imgFileRequest)

                            val titulo: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),nome.text.toString())
                            val descricao: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),descri.text.toString())
                            val lat: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),latitude)
                            val lon: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),longitude)
                            val utilizador: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),userid.toString())
                            val tipo: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),spinner.selectedItem.toString())


                            val call = request.inserirOcorr(titulo,descricao,lat,lon,imagem,utilizador,tipo)
                            call.enqueue(object : Callback<Problema> {
                                override fun onResponse(call: Call<Problema>, response: Response<Problema>) {
                                    if (response.isSuccessful){
                                        if(response.body()!!.status){
                                            Toast.makeText(this@CriarOccor, getString(R.string.criadosucesso), Toast.LENGTH_LONG).show()
                                            finish()
                                        }else{
                                            Toast.makeText(this@CriarOccor, getString(R.string.erro), Toast.LENGTH_LONG).show()
                                        }

                                    }
                                }
                                override fun onFailure(call: Call<Problema>, t: Throwable) {
                                    Toast.makeText(this@CriarOccor, "${t.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                            finish()
                        }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            image_view.setImageBitmap(imageBitmap)
        }
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@CriarOccor.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}