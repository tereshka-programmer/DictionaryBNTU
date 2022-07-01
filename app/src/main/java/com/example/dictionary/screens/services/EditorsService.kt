package com.example.dictionary.screens.services

import android.util.Log
import com.example.dictionary.model.PrivateUser
import com.example.dictionary.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

typealias EditorServiceListener = (editors: List<PrivateUser>) -> Unit

class EditorsService {
    private var editors = mutableListOf<PrivateUser>()

    private var listeners = mutableSetOf<EditorServiceListener>()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            editors = try {
                RetrofitInstance.api.getAllEditors().toMutableList()
            } catch (e: IOException){
                Log.e("MainAct", "IOException, you might not have internet connection")
                return@launch
            }
        }
    }

    fun deleteEditor(editor: PrivateUser){
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.api.delEditor(editor.id.toString())
            if (response.isSuccessful){
                val indexToDelete = editors.indexOfFirst { it.id == editor.id }
                if (indexToDelete != -1) editors.removeAt(indexToDelete)
            }
            notifyChanges()
        }
    }

    fun updateEditor(editor: PrivateUser){
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.api.updateEditor(editor)
            if (response.isSuccessful){
                val indexOfEditor = editors.indexOfFirst { it.id == editor.id }
                if (indexOfEditor != -1 ) editors[indexOfEditor] = editor
            }
            notifyChanges()
        }
    }

    fun createEditor(editor: PrivateUser){
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitInstance.api.addNewEditor(editor)
            if (response.isSuccessful){
                editors.add(editor)
            }
            notifyChanges()
        }
    }

    fun addListener(listener: EditorServiceListener){
        listeners.add(listener)
        listener.invoke(editors)
    }

    fun removeListener(listener: EditorServiceListener){
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(editors)}
    }

}