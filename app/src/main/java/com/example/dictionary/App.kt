package com.example.dictionary

import android.app.Application
import com.example.dictionary.screens.services.EditorsService
import com.example.dictionary.screens.services.WordsEditorService

class App : Application() {

    val wordsEditorService = WordsEditorService()
    val editorService = EditorsService()
}