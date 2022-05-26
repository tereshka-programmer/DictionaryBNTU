package com.example.dictionary.retrofit

import com.example.dictionary.model.Word
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordApi {

    @GET("/word/ru/{word}")
    suspend fun getWordRu(@Path("word") word: String): Word

    @GET("/word/ch/{word}")
    suspend fun getWordCh(@Path("word") word: String): Word

    @GET("/word/en/{word}")
    suspend fun getWordEn(@Path("word") word: String): Word

}