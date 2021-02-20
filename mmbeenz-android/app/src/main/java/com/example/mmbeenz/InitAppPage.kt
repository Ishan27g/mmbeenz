package com.example.mmbeenz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.schedule

class InitAppPage: AppCompatActivity() {

    private lateinit var splashImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.init_app_page)


        Timer().schedule(4000) {
            launchApp()
        }

        splashImage = findViewById(R.id.splash_image)
        splashImage.animate().translationY((-1600).toFloat()).setDuration(2000).startDelay = 2000

    }
    private fun launchApp(){
        val signupActivityIntent = Intent(this@InitAppPage, SignupActivity::class.java)
        startActivity(signupActivityIntent)
    }
}
