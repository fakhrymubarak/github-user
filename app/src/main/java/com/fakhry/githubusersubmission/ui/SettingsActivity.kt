package com.fakhry.githubusersubmission.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.githubusersubmission.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, SettingsFragment())
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}