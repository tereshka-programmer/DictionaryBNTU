package com.example.dictionary.retrofit

import com.example.dictionary.model.PrivateUser
import com.example.dictionary.model.Word
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface WordApi {

    @GET("/word")
    suspend fun getAllWords(): List<Word>

    @GET("/word/ru/{word}")
    suspend fun getWordRu(@Path("word") word: String): Word

    @GET("/word/ch/{word}")
    suspend fun getWordCh(@Path("word") word: String): Word

    @GET("/word/en/{word}")
    suspend fun getWordEn(@Path("word") word: String): Word

    @DELETE("/word/{id}")
    suspend fun delWord(@Path("id") id: String): Response<ResponseBody>

    @PUT("/word")
    suspend fun updateWord(@Body word: Word): Response<ResponseBody>

    @POST("/word")
    suspend fun addNewWord(@Body word: Word): Response<ResponseBody>

    @GET("/privateUsers")
    suspend fun getAllEditors(): List<PrivateUser>

    @DELETE("/privateUsers/{id}")
    suspend fun delEditor(@Path("id") id: String): Response<ResponseBody>

    @PUT("/privateUsers")
    suspend fun updateEditor(@Body editor: PrivateUser): Response<ResponseBody>

    @POST("/privateUsers")
    suspend fun addNewEditor(@Body editor: PrivateUser): Response<ResponseBody>

    @GET("/privateUsers/{login}")
    suspend fun getEditorForLogin(@Path("login") login: String): PrivateUser

}