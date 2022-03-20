package com.example.trivia_app.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
  @GET
  suspend fun getTrivia(@Url url:String) :Response<TriviaResponse>
}