package com.example.dictionary.screens

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary.App
import com.example.dictionary.R
import com.example.dictionary.databinding.UpdateDialogItemBinding
import com.example.dictionary.databinding.WordsListFragmentBinding
import com.example.dictionary.model.Word
import com.example.dictionary.screens.adapters.UserActionListener
import com.example.dictionary.screens.adapters.WordsEditorAdapter
import com.example.dictionary.screens.services.WordsEditorService
import com.example.dictionary.screens.services.WordsServiceListener

class WordsListFragment : Fragment(R.layout.words_list_fragment) {

    private lateinit var binding: WordsListFragmentBinding
    private lateinit var adapter: WordsEditorAdapter

    // May be it need to change requireContext() on activity.applicationContext, because the second context depends on lifecycle all app
    private val wordsEditorService : WordsEditorService
        get() = (activity?.applicationContext as App).wordsEditorService


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WordsListFragmentBinding.bind(view)

        adapter = WordsEditorAdapter(object : UserActionListener {
            override fun onWordDelete(word: Word) {
                wordsEditorService.deleteWord(word)
            }

            override fun updateWord(word: Word) {
                wordsEditorService.updateWord(word)
            }

            override fun createWord(word: Word) {
                wordsEditorService.createWord(word)
            }
        })
//        lifecycleScope.launchWhenCreated {
//            val someList = try {
//                RetrofitInstance.api.getAllWords()
//            } catch (e: IOException){
//                Log.e("MainAct", "IOException, you might not have internet connection")
//                return@launchWhenCreated
//            }
//            adapter.wordsList = someList
//        }


        val layoutManager = LinearLayoutManager(requireContext())
        binding.rcvWords.layoutManager = layoutManager
        binding.rcvWords.adapter = adapter

        wordsEditorService.addListener(wordsListener)

        binding.button2.setOnClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val itemBinding = UpdateDialogItemBinding.inflate(inflater)

            val listener = DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE){
                    wordsEditorService.createWord(
                        Word(
                            id = null,
                            enWord = itemBinding.edtEnWord.text.toString(),
                            ruWord = itemBinding.edtRuWord.text.toString(),
                            chWord = itemBinding.edtChWord.text.toString(),
                            chTranscript = itemBinding.edtChTranscrip.text.toString()
                        )
                    )
                }
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("New Word")
                .setView(itemBinding.root)
                .setPositiveButton("Create", listener)
                .create()




//            dialog.setOnShowListener{
//                itemBinding.edtEnWord.requestFocus()
//                showKeyboard(itemBinding.edtEnWord)
//
//                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
//                    val enText = itemBinding.edtEnWord.text.toString()
//                    val ruText = itemBinding.edtRuWord.text.toString()
//                    val chText = itemBinding.edtChWord.text.toString()
//                    val chtText = itemBinding.edtChTranscrip.text.toString()
//
//                    if (enText.isBlank()){
//                        itemBinding.edtEnWord.error = "Value is empty"
//                    } else if (ruText.isBlank()){
//                        itemBinding.edtRuWord.error = "Value is empty"
//                    } else if (chText.isBlank()){
//                        itemBinding.edtChWord.error = "Value is empty"
//                    } else if (chtText.isBlank()){
//                        itemBinding.edtChTranscrip.error = "Value is empty"
//                    }
//
//                    wordsEditorService.createWord(
//                        Word(
//                            id = "assa",
//                            enWord = itemBinding.edtEnWord.text.toString(),
//                            ruWord = itemBinding.edtRuWord.text.toString(),
//                            chWord = itemBinding.edtChWord.text.toString(),
//                            chTranscript = itemBinding.edtChTranscrip.text.toString()
//                        )
//                    )
//                    dialog.dismiss()
//                }
//            }
//            dialog.setOnDismissListener { hideKeyboard(itemBinding.edtEnWord) }
            dialog.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        wordsEditorService.removeListener(wordsListener)
    }

    private val wordsListener: WordsServiceListener = {
        adapter.wordsList = it
    }


    private fun showKeyboard(view: View){
        view.post {
            getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(view: View){
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
}
