package com.example.trivia_app.api

data class Trivia(
  val category: String,
  val type: String,
  val difficult: String,
  val question: String,
  val correct_answer: String,
  val incorrect_answers: Array<String>
)
