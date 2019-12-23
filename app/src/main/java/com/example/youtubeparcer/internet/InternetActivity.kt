package com.example.youtubeparcer.internet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.youtubeparcer.R
import com.example.youtubeparcer.ui.MainActivity

class InternetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet)
    }

    fun onClick(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
