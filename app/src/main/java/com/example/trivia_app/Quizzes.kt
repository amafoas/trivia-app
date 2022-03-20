package com.example.trivia_app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.forEach
import com.example.trivia_app.api.APIService
import com.example.trivia_app.api.Trivia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Quizzes : AppCompatActivity() {
  private lateinit var categoryName: String
  private lateinit var categoryID: String
  private lateinit var difficulty: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quizzes)

    categoryName = intent.getStringExtra("categoryName") ?: ""
    categoryID = intent.getStringExtra("categoryID") ?: "0"
    difficulty = intent.getStringExtra("difficulty") ?: "easy"

    val cat = findViewById<TextView>(R.id.cat_category_title)
    cat.text = getString(R.string.quizz_category, categoryName)
    val dif = findViewById<TextView>(R.id.cat_difficulty_title)
    dif.text = getString(R.string.quizz_difficulty, difficulty)

    val answerButtons = findViewById<LinearLayout>(R.id.answer_buttons)
    answerButtons.forEach { setBackgroundColorToDefault(it as Button) }

    getQuizzesFromApi(categoryID, difficulty)
  }

  private fun loadNewTrivia(trivias: Iterator<Trivia>, score: Int){
    if (trivias.hasNext()) {
      setTriviaTo(trivias, trivias.next(), score)
    } else {
      goToScoreScreen(score)
    }
  }

  private fun setTriviaTo(trivias: Iterator<Trivia>, trivia: Trivia, score: Int){
    // Change the title of the question
    findViewById<TextView>(R.id.question).text = parseHTMLText(trivia.question)

    Log.d("Trivia correct", trivia.correct_answer)

    val answers = randomsAnswersIterator(trivia)
    val answerButtons = findViewById<LinearLayout>(R.id.answer_buttons)

    answerButtons.forEach {
      val answer = if (answers.hasNext()) { parseHTMLText(answers.next()) } else ""

      val btn = it as Button
      btn.text = answer
      btn.setOnClickListener {
        val correct = answer == trivia.correct_answer
        btn.setBackgroundColor( if (correct) Color.GREEN else Color.RED )

        areAllClickable(answerButtons, false)
        Handler(Looper.getMainLooper()).postDelayed({
          setBackgroundColorToDefault(btn)
          loadNewTrivia(trivias, score + if (correct) 1 else 0)
          areAllClickable(answerButtons, true)
        }, 450)
      }
    }
  }

  private fun showLoadingScreen(state: Boolean){
    val children = findViewById<ConstraintLayout>(R.id.quizzes_container)
    children.forEach {
      it.visibility = if (state) View.INVISIBLE else View.VISIBLE
    }
    val loaderIcon = findViewById<ProgressBar>(R.id.quizz_loader)
    loaderIcon.visibility = if (state) View.VISIBLE else View.GONE
  }

  private fun goToScoreScreen(score: Int){
    val intent = Intent(this, ScoreScreen::class.java)
    intent.putExtra("score", score)
    intent.putExtra("categoryName", categoryName)
    intent.putExtra("categoryID", categoryID)
    intent.putExtra("difficulty", difficulty)
    startActivity(intent)
    finish()
  }

  /// API CALL
  private fun getRetrofit(): Retrofit{
    return Retrofit.Builder()
      .baseUrl("https://opentdb.com/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private fun getQuizzesFromApi(categoryID: String, difficulty: String){
    showLoadingScreen(true)
    CoroutineScope(Dispatchers.IO).launch {
      val call = getRetrofit()
        .create(APIService::class.java)
        .getTrivia("api.php?amount=5&category=$categoryID&difficulty=$difficulty&type=multiple")

      val body = call.body()
      runOnUiThread {
        if (body != null && body.response_code == "0"){
          loadNewTrivia(body.results.iterator(), 0)
        }
        showLoadingScreen(false)
      }
    }
  }

  /// HELPERS
  private fun parseHTMLText(str: String): String {
    return HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
  }
  private fun setBackgroundColorToDefault(btn: Button){
    btn.setBackgroundColor(getColor(R.color.lightRed))
  }
  private fun areAllClickable(buttonGroup: ViewGroup, status: Boolean) {
    buttonGroup.forEach { (it as Button).isClickable = status }
  }
  private fun randomsAnswersIterator(trivia: Trivia): Iterator<String>{
    val answers = trivia.incorrect_answers.plus(trivia.correct_answer)
    answers.shuffle()
    return answers.iterator()
  }
}