package com.example.mmbeenz

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.concurrent.schedule


class ImagePick : AppCompatActivity() {

    private lateinit var usernameV: TextView
    private lateinit var photoV: ImageView
    private lateinit var button: Button

    private var username: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pick)

        username = intent.getStringExtra("username").toString()
        password = intent.getStringExtra("username").toString()

        usernameV = findViewById(R.id.profile_name)
        photoV = findViewById(R.id.upload_imageView)
        button = findViewById(R.id.upload_button)

        usernameV.text = username

        button.setOnClickListener{selectImage()}

    }

    /*
        Gallery launcher and image picker code sourced from:
        https://devofandroid.blogspot.com/2018/09/pick-image-from-gallery-android-studio_15.html
    * */

    private fun selectImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                pickImageFromGallery()
            }
        }
        else{
            pickImageFromGallery()
        }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/png"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val imgUri = data?.data
            photoV.setImageURI(imgUri)


            if (imgUri != null) {
                println(imgUri.path)
                Toast.makeText(this, "Registering...", Toast.LENGTH_LONG).show()
                Timer().schedule(4000){
                    registerUser(imgUri)
                }
            }
        }
    }
    private fun registerUser(imgUri: Uri){
        var host ="http://10.0.2.2:7000"

        var mimeType =""
        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUri.path)
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).toString()
        }

        /*
         Parse android source path -
         Sourced from http://www.androidsnippets.com/get-file-path-of-gallery-image.html
        */
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contentResolver.query(imgUri, proj, null, null)
        } else TODO("VERSION.SDK_INT < O")
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val x = cursor.getString(column_index)
        val imgFile = File(x)

        val requestBody: RequestBody =
            MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "profile_photo",
                    "$username.png",
                    imgFile.asRequestBody(mimeType.toMediaTypeOrNull())
                )
                .build()

        val auth = password?.let { username?.let { u -> Credentials.basic(u, it) } }!!
        val request: Request = Request
            .Builder().addHeader("Authorization", auth)
            .url("$host/register?username=$username&password=$password")
            .post(requestBody).build()

        val client = OkHttpClient()
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            println("received rsp : $response")
            val homePageIntent = Intent(this@ImagePick, HomeActivity::class.java)
            homePageIntent.putExtra("username", username)
            homePageIntent.putExtra("password", password)
            startActivity(homePageIntent)
        }
        else {
            println(response.body.toString())
            println("Error : %s".format(response.message))
            Toast.makeText(this, "Error : %s".format(response.body.toString()), Toast.LENGTH_SHORT).show()
        }

    }

}


