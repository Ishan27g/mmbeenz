package com.example.mmbeenz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray

class HomeActivity : AppCompatActivity() {

    var allUsers= mutableListOf<UserItem>()

    lateinit var currUser: String
    lateinit var refreshActionView: SwipeRefreshLayout
    val host ="http://10.0.2.2:7000"
    lateinit var userRecyclerView: RecyclerView
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        supportActionBar?.hide()

        refreshActionView = findViewById(R.id.home_page_refresh)
        refreshActionView.setOnRefreshListener {
            getAllUser()
            refreshActionView.isRefreshing = false
        }
        userRecyclerView = findViewById(R.id.user_recycler_view)

        currUser = intent.getStringExtra("username").toString()
        password = intent.getStringExtra("password").toString()

        getAllUser()
    }
    private fun parseToUserObject(response: JSONArray): Boolean{
        for (i in 0 until response.length()){
            val user = response.getJSONObject(i)
            val thisUser = user.optString("username") //don't display profile for current User
            if(thisUser.compareTo(currUser) != 0) allUsers.add(UserItem(user.optString("username"), user.getInt("rating")))
        }
        return (allUsers.size > 0)
    }
    private fun getAllUser(){
        var queue= Volley.newRequestQueue(this)
        val getAllUsersRequest = object : JsonArrayRequest(
            Method.GET, "$host/allUsers", null,
            { response ->
                println("AllUsers ->$response")
                if (parseToUserObject(response)) {
                    userRecyclerView.adapter = UserCardAdapter(allUsers)
                    userRecyclerView.layoutManager = LinearLayoutManager(this)
                    userRecyclerView.setHasFixedSize(true)
                }
                 Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show()
            },
            { error ->
                println("Error : getting all users ->%s".format(error.message.toString()))
                Toast.makeText(
                    this,
                    "Error : Refreshing",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                val cred = String.format("%s:%s", currUser,password)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params

            }
        }
        queue.add(getAllUsersRequest)
    }
}

