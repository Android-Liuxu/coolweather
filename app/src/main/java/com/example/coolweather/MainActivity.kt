package com.example.coolweather

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (pref.getString("location",null)!=null) {
            val intent:Intent = Intent(this,WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
