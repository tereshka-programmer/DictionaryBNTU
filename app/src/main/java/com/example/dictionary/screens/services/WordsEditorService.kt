package com.example.dictionary.screens.services

import android.util.Log
import com.example.dictionary.model.Word
import com.example.dictionary.retrofit.RetrofitInstance
import kotlinx.coroutines.*
import java.io.IOException

typealias WordsServiceListener = (words: List<Word>) -> Unit

class WordsEditorService {

    private var words = mutableListOf<Word>()

    private var listeners = mutableSetOf<WordsServiceListener>()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            words = try {
                RetrofitInstance.api.getAllWords().toMutableList()
            } catch (e: IOException){
                Log.e("MainAct", "IOException, you might not have internet connection")
                return@launch
            }
        }
    }

    //Need modify
    fun deleteWord(word: Word){
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.api.delWord(word.id.toString())
            if (response.isSuccessful){
                val indexToDelete = words.indexOfFirst { it.id == word.id }
                if (indexToDelete != -1) words.removeAt(indexToDelete)
            }
            notifyChanges()
        }
    }

    fun updateWord(word: Word){
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.api.updateWord(word)
            if (response.isSuccessful){
                val indexOfWord = words.indexOfFirst { it.id == word.id }
                if (indexOfWord != -1) words[indexOfWord] = word
            }
            notifyChanges()
        }
    }

    fun createWord(word: Word){
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.api.addNewWord(word)
            if (response.isSuccessful) words.add(word)
            notifyChanges()
        }
    }

    fun addListener(listener: WordsServiceListener){
        listeners.add(listener)
        listener.invoke(words)
    }

    fun removeListener(listener: WordsServiceListener){
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(words)}
    }

    fun getWordsRV(): List<Word> = words

}