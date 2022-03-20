package com.example.trivia_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val startBtn = findViewById<Button>(R.id.btn_start)
    startBtn.setOnClickListener{
      val intent = Intent(this, Difficulties::class.java)
      startActivity(intent)
    }
  }
}