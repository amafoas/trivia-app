package com.example.trivia_app.api

data class TriviaResponse (
  val response_code: String,
  val results: Array<Trivia>
  )