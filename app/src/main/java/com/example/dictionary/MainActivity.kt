package com.example.dictionary


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.model.Word
import com.example.dictionary.retrofit.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException


class MainActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMainBinding

    var languageCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                languageCode = parent.getItemAtPosition(position).toString()
                Toast.makeText(parent.context, parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.edWord.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideSoftKeyboard(binding.edWord)
                    getWord(binding.edWord.text.toString().lowercase())
                    true
                }
                else -> false
            }
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getWord(word: String){
        if (TextUtils.isEmpty(binding.edWord.text)) {
            binding.edWord.setBackgroundResource(R.drawable.spinner_back_error)
            binding.tvErrorNT.isVisible = true
            return
        }
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                when(languageCode){
                    "English" -> RetrofitInstance.api.getWordEn(word)
                    "Русский" -> RetrofitInstance.api.getWordRu(word)
                    "中文" -> RetrofitInstance.api.getWordCh(word)
                    else -> Word("ErrorLanguageCode", "Choose", "The", "Language", "Please")
                }
            } catch (e: IOException) {
                Log.e("MainAct", "IOException, you might not have internet connection")

                AlertDialog.Builder(this@MainActivity)
                    .setCancelable(true)
                    .setTitle("Error")
                    .setMessage("The Internet may be turned off, or this is our problem, we will fix it soon")
                    .create()
                    .show()

                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("MainAct", "HttpException, unexpected response")
                AlertDialog.Builder(this@MainActivity)
                    .setCancelable(true)
                    .setTitle("Error")
                    .setMessage("Something is wrong, we will fix it soon")
                    .create()
                    .show()
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            binding.progressBar.isVisible = false
            binding.edWord.text.clear()

            if (response.id != "NSWERROR") {
                binding.tvFirstRusTrans.text = response.ruWord
                binding.tvFirstChinTrans.text = response.chWord
                binding.tvFirstEngTrans.text = response.enWord
                binding.tvTranscrip.text = response.chTranscript
            } else {
                binding.edWord.setBackgroundResource(R.drawable.spinner_back_error)
                binding.tvErrorNSW.isVisible = true
                binding.edWord.text.clear()
            }
        }
    }

    fun tapOnED(view: View){
        binding.tvErrorNSW.isVisible = false
        binding.tvErrorNT.isVisible = false
        binding.edWord.setBackgroundResource(R.drawable.spinner_back)
    }


}