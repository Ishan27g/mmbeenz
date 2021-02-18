package com.example.mmbeenz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)
        supportActionBar?.hide()


    }

}

