package com.example.cm

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_criar_occor.*

class CriarOccor : AppCompatActivity() {

    private lateinit var nome: EditText
    private lateinit var descricao: EditText
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_occor)


        nome = findViewById(R.id.titulo)
        descricao = findViewById(R.id.descr)

        btncamera.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }

        }

        val button = findViewById<Button>(R.id.button7)
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

    private fun openCamera() {
        val values = ContentValues()
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(image_uri)
        }
    }
    companion object {
        const val EXTRA_REPLY_NOME = "nome"
        const val EXTRA_REPLY_DESCRICAO = "descricao"
    }



}