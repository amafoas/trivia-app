package com.example.trivia_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Difficulties : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_difficulties)

    val easyBtn   = findViewById<Button>(R.id.btn_easy)
    easyBtn.setOnClickListener { goToCategories("easy") }

    val mediumBtn = findViewById<Button>(R.id.btn_medium)
    mediumBtn.setOnClickListener { goToCategories("medium") }

    val hardBtn   = findViewById<Button>(R.id.btn_hard)
    hardBtn.setOnClickListener { goToCategories("hard") }
  }

  private fun goToCategories(difficulty: String){
    val intent = Intent(this, Categories::class.java)
    intent.putExtra("difficulty", difficulty)
    startActivity(intent)
  }
}