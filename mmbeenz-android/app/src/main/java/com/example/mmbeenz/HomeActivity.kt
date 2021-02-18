package com.example.mmbeenz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class HomeActivity : AppCompatActivity() {

    var allUsers= mutableListOf<UserItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        supportActionBar?.hide()

        val username = intent.getStringExtra("username").toString()
        val password = intent.getStringExtra("password").toString()
        val userRecyclerView = findViewById<RecyclerView>(R.id.user_recycler_view)

        var queue= Volley.newRequestQueue(this)
        val host ="http://10.0.2.2:7000"
        val getAllUsersRequest = object : JsonArrayRequest(
            Method.GET, "$host/allUsers", null,
            { response ->
                println("AllUsers ->$response")
                if (parseToUserObject(response)) {
                    println(allUsers.toString())
                    userRecyclerView.adapter = UserCardAdapter(allUsers)
                    userRecyclerView.layoutManager = LinearLayoutManager(this)
                    userRecyclerView.setHasFixedSize(true)
                }
                // Toast.makeText(this, "$response", Toast.LENGTH_LONG).show()
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

        queue.add(getAllUsersRequest)

    }
    private fun parseToUserObject(response: JSONArray): Boolean{
        for (i in 0 until response.length()){
            val user = response.getJSONObject(i)
            allUsers.add(UserItem(1,user.optString("username"), user.getInt("rating")))
        }
        return (allUsers.size > 0)
    }
}

