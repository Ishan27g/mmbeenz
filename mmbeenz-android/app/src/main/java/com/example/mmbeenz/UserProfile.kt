package com.example.mmbeenz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    lateinit var imageV: ImageView
    lateinit var usernameV: TextView
    lateinit var ratingV: RatingBar
    lateinit var submit: TextView

    var targetUser= ""
    var defaultRating = 2
    var rating= defaultRating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        imageV = findViewById(R.id.user_profile_image_view)
        usernameV = findViewById(R.id.user_profile_name_view)
        ratingV = findViewById(R.id.user_profile_rating_bar)
        submit = findViewById(R.id.user_profile_submit)

        targetUser = intent.getStringExtra("username").toString()
        rating = intent.getIntExtra("rating", defaultRating.toInt())

        val host ="http://10.0.2.2:7000"
        Picasso.get().load("$host/image/$targetUser.png").into(imageV)

        usernameV.text = targetUser
        ratingV.numStars = 5
        ratingV.rating = rating.toFloat()

        submit.setOnClickListener{rateUser()}
    }
    private fun rateUser(){
        println(ratingV.rating)
        var queue= Volley.newRequestQueue(this)
        val host ="http://10.0.2.2:7000"

        val stringRequest  = object : StringRequest(
            Method.POST, "$host/user?name=$targetUser&rating=${ratingV.rating.toInt()}",
            { response ->
                println("received rsp : $response")
            },
            { error ->
                println("Error : %s".format(error.message.toString()))
                Toast.makeText(this, "Error : %s".format(error.message.toString()), Toast.LENGTH_SHORT).show()
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                return superUser()

            }
        }



        queue.add(stringRequest)
    }
}

fun superUser(): MutableMap<String, String> {
    val params = HashMap<String, String>()
    val cred = String.format("%s:%s", "Abed", "password")
    val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.DEFAULT)
    params["Authorization"] = auth
    return params
}
