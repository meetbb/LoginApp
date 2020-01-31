package com.mosfl.example.logindemo.views

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mosfl.example.logindemo.R

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val infoTextView = findViewById<TextView>(R.id.infoTextView)
        val intentBundle: Bundle? = intent.extras
        val userName = intentBundle!!.getString("USER_NAME") // 1
        infoTextView.text = userName
    }
}