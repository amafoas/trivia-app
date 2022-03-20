package com.example.trivia_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ScoreScreen : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_score_screen)

    val score = intent.getIntExtra("score", 0)
    val categoryName = intent.getStringExtra("categoryName")
    val categoryID = intent.getStringExtra("categoryID")
    val difficulty = intent.getStringExtra("difficulty")

    val scoreView = findViewById<TextView>(R.id.score)
    scoreView.text = score.toString()

    val returnBtn = findViewById<Button>(R.id.return_menu_btn)
    returnBtn.setOnClickListener {
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
      finish()
    }

    val playAgainBtn = findViewById<Button>(R.id.play_again_btn)
    playAgainBtn.setOnClickListener {
      val intent = Intent(this, Quizzes::class.java)
      intent.putExtra("categoryName", categoryName)
      intent.putExtra("categoryID", categoryID)
      intent.putExtra("difficulty", difficulty)
      startActivity(intent)
      finish()
    }
  }
}