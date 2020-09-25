package com.fakhry.githubusersubmission.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.githubusersubmission.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                delay(2000)
                val intent = Intent(
                    this@SplashScreenActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("onBoarding", e.message.toString())
            }
        }
    }
}
