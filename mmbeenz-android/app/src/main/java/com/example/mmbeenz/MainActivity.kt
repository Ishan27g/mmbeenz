package com.example.mmbeenz

import android.os.Bundle
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    lateinit var homePageData:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val username = intent.getStringExtra("username").toString()
        val password = intent.getStringExtra("password").toString()

        println("ok 2: $username $password")


        homePageData = findViewById(R.id.homepage_textview)
        var queue= Volley.newRequestQueue(this)
        val host ="http://10.0.2.2:7000"
        val getAllUsersRequest = object : JsonArrayRequest(
            Method.GET, "$host/allUsers", null,
            { response ->
                println("AllUsers ->$response")

               // Toast.makeText(this, "$response", Toast.LENGTH_LONG).show()
                homePageData.text = response.toString()
            },
            { error ->
                println("Error : getting all users ->%s".format(error.message.toString()))
                Toast.makeText(
                    this,
                    "Error : %s".format(error.message.toString()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                val cred = String.format("%s:%s", username,password)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params

            }
        }

        println(getAllUsersRequest.headers.toString())
        queue.add(getAllUsersRequest)

    }
}

