package com.example.mmbeenz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import java.security.MessageDigest

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
        password = passwordView.text.toString()//.sha512()

        // Hide the keyboard.
        hideKeyboard(currentFocus ?: View(this))

        val uploadImageIntent = Intent(this@SignupActivity, ImagePick::class.java)
        uploadImageIntent.putExtra("username", username)
        uploadImageIntent.putExtra("password", password)
        startActivity(uploadImageIntent)


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
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

fun String.sha512(): String {
    return this.hashWithAlgorithm("SHA-512")
}

private fun String.hashWithAlgorithm(algorithm: String): String {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("", { str, it -> str + "%02x".format(it) })
}
