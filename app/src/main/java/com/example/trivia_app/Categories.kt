package com.example.trivia_app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.core.view.forEach

class Categories : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_categories)

    val difficulty = intent.getStringExtra("difficulty") ?: "easy"
    val colors = randomColorsIDIterator()
    val categories = arrayOf(
      "category_geography", "category_books", "category_sports", "category_film",
      "category_music", "category_computers", "category_mathematics", "category_videogames"
    ).iterator()

    val btnGrid = findViewById<GridLayout>(R.id.button_grid)
    btnGrid.forEach {
      val currentCategory = if (categories.hasNext()) categories.next() else ""
      val categoryName = getTextFromResources(currentCategory)
      val btn = it as Button
      btn.text = categoryName
      btn.setBackgroundColor(if (colors.hasNext()) colors.next() else 0)
      btn.setOnClickListener {
        goToQuizzes(categoryID(currentCategory), categoryName, difficulty)
      }
    }
  }

  private fun randomColorsIDIterator(): Iterator<Int>{
    val colors = resources.getStringArray(R.array.colors)
    val parsedColors = mutableListOf<Int>()
    for (color in colors){
      parsedColors.add(Color.parseColor(color))
    }
    parsedColors.shuffle()
    return parsedColors.iterator()
  }

  private fun getTextFromResources(name: String): String {
    return resources.getString(resources.getIdentifier(name, "string", packageName))
  }

  private fun categoryID(name: String): String {
    return when (name) {
      "category_books"       -> "10"
      "category_film"        -> "11"
      "category_music"       -> "12"
      "category_videogames"  -> "15"
      "category_computers"   -> "18"
      "category_mathematics" -> "19"
      "category_sports"      -> "21"
      "category_geography"   -> "22"
      else -> "10"
    }
  }

  private fun goToQuizzes(categoryID: String, categoryName: String, difficulty: String){
    val intent = Intent(this, Quizzes::class.java)
    intent.putExtra("categoryName", categoryName)
    intent.putExtra("categoryID", categoryID)
    intent.putExtra("difficulty", difficulty)
    startActivity(intent)
  }
}