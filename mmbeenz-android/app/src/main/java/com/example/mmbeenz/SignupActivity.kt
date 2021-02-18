package com.example.mmbeenz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class SignupActivity : AppCompatActivity() {

    lateinit var signupBtn: Button
    lateinit var loginBtn: Button

    lateinit var usernameView: EditText
    lateinit var passwordView: EditText

    private var username: String? = null
    private var password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        supportActionBar?.hide()

        usernameView  = findViewById(R.id.username_text)
        passwordView = findViewById(R.id.password_text)
        signupBtn  = findViewById(R.id.signup_button)
        loginBtn = findViewById(R.id.login_button)

        signupBtn.setOnClickListener{ signUpHandler()}
        loginBtn.setOnClickListener{ loginHandler()}


    }
    private fun signUpHandler(){
        username = usernameView.text.toString()
        password = passwordView.text.toString() // add a hash function


        // Hide the keyboard.
        hideKeyboard(currentFocus ?: View(this))

        var queue= Volley.newRequestQueue(this)
        val host ="http://10.0.2.2:7000"

        val stringRequest = StringRequest(Request.Method.POST, "$host/register?username=$username&password=$password",
            { response ->
                println("received rsp : $response")
                Toast.makeText(this, "Registered as: $username", Toast.LENGTH_LONG).show()

                val homePageIntent = Intent(this@SignupActivity, HomeActivity::class.java)
                homePageIntent.putExtra("username", username)
                homePageIntent.putExtra("password", password)
                startActivity(homePageIntent)
            },
            { error ->
                println("Error : %s".format(error.message.toString()))
                Toast.makeText(this, "Error : %s".format(error.message.toString()), Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(stringRequest)
    }
    private fun loginHandler(){
        username = usernameView.text.toString()
        password = passwordView.text.toString() // add a hash function
        val homePageIntent = Intent(this@SignupActivity, HomeActivity::class.java)

        homePageIntent.putExtra("username", username)
        homePageIntent.putExtra("password", password)
        startActivity(homePageIntent)
        // Hide the keyboard.
        hideKeyboard(currentFocus ?: View(this))
        clearText()
    }
    private fun clearText(){
        usernameView.setText("")
        passwordView.setText("")
    }
    private fun getUsername(): String? {
       return username
    }
    private fun getPassword(): String? {
        return password
    }
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

