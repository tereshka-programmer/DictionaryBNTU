package com.example.dictionary.screens

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.R
import com.example.dictionary.databinding.MainAdminFragmentBinding
import com.example.dictionary.model.Word
import com.example.dictionary.retrofit.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class MainAdminFragment : Fragment(R.layout.main_admin_fragment) {
    private lateinit var binding: MainAdminFragmentBinding

    private var languageCode = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainAdminFragmentBinding.bind(view)


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
            binding.tvErrorNSW.isVisible = false
            binding.tvErrorNT.isVisible = false
            binding.edWord.setBackgroundResource(R.drawable.spinner_back)
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard(binding.edWord)
                    getWord(binding.edWord.text.toString().lowercase())
                    true
                }
                else -> false
            }
        }

    }

    private fun hideKeyboard(view: View){
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = requireContext()
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

                AlertDialog.Builder(requireContext())
                    .setCancelable(true)
                    .setTitle("Error")
                    .setMessage("The Internet may be turned off, or this is our problem, we will fix it soon")
                    .create()
                    .show()

                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("MainAct", "HttpException, unexpected response")
                AlertDialog.Builder(requireContext())
                    .setCancelable(true)
                    .setTitle("Error")
                    .setMessage("Something is wrong, we will fix it soon")
                    .create()
                    .show()
                binding.progressBar.isVisible = false
                binding.edWord.setBackgroundResource(R.drawable.spinner_back_error)
                binding.tvErrorNSW.isVisible = true
                binding.edWord.text.clear()
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

}