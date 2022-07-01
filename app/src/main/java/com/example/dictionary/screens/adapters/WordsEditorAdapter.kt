package com.example.dictionary.screens.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.R
import com.example.dictionary.databinding.ItemWordBinding
import com.example.dictionary.databinding.UpdateDialogItemBinding
import com.example.dictionary.model.Word

interface UserActionListener{
    fun onWordDelete(word: Word)

    fun updateWord(word: Word)

    fun createWord(word: Word)
}


class WordsEditorAdapter(
    private val userActionListener: UserActionListener
) : RecyclerView.Adapter<WordsEditorAdapter.WordsViewHolder>() , View.OnClickListener {

    var wordsList: List<Word> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordsEditorAdapter.WordsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWordBinding.inflate(inflater, parent, false)

        binding.icMoreWordItem.setOnClickListener(this)

        return WordsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsEditorAdapter.WordsViewHolder, position: Int) {
        val word = wordsList[position]
        with(holder.binding){
            icMoreWordItem.tag = word

            enWord.text = word.enWord
            ruWord.text = word.ruWord
            chWord.text = word.chWord
            chTranscrip.text = word.chTranscript
        }
    }

    override fun getItemCount(): Int = wordsList.size

    class WordsViewHolder(
        val binding: ItemWordBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val word = v.tag as Word
        if (v.id == R.id.icMoreWordItem) showPopUpMenu(v)
    }

    private fun showPopUpMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        var word = view.tag as Word

        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Delete")
        popupMenu.menu.add(0, ID_REDACT, Menu.NONE, "Edit")

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                ID_REMOVE -> {
                    showDialogForDelete(view)
                }
                ID_REDACT -> {
                    showDialogForUpdate(view)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    private fun showDialogForUpdate(v: View){
        val inflater = LayoutInflater.from(v.context)
        val binding = UpdateDialogItemBinding.inflate(inflater)
        val word = v.tag as Word
        with(binding){
            edtEnWord.setText(word.enWord)
            edtRuWord.setText(word.ruWord)
            edtChWord.setText(word.chWord)
            edtChTranscrip.setText(word.chTranscript)
        }

        val dialog =AlertDialog.Builder(v.context)
            .setTitle("Edit")
            .setView(binding.root)
            .setPositiveButton("Update", null)
            .create()

        dialog.setOnShowListener{
            binding.edtEnWord.requestFocus()
            showKeyboard(binding.edtChWord)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enText = binding.edtEnWord.text.toString()
                val ruText = binding.edtRuWord.text.toString()
                val chText = binding.edtChWord.text.toString()
                val chtText = binding.edtChTranscrip.text.toString()

                if (enText.isBlank()){
                    binding.edtEnWord.error = "Value is empty"
                    return@setOnClickListener
                } else if (ruText.isBlank()){
                    binding.edtRuWord.error = "Value is empty"
                    return@setOnClickListener
                } else if (chText.isBlank()){
                    binding.edtChWord.error = "Value is empty"
                    return@setOnClickListener
                } else if (chtText.isBlank()){
                    binding.edtChTranscrip.error = "Value is empty"
                    return@setOnClickListener
                }

                userActionListener.updateWord(
                    Word(
                        id = word.id,
                        enWord = binding.edtEnWord.text.toString(),
                        ruWord = binding.edtRuWord.text.toString(),
                        chWord = binding.edtChWord.text.toString(),
                        chTranscript = binding.edtChTranscrip.text.toString()
                    )
                )
                dialog.dismiss()
            }
        }
        dialog.setOnDismissListener { hideKeyboard(binding.edtEnWord) }
        dialog.show()
    }

    private fun showDialogForCreate(v: View){
        val inflater = LayoutInflater.from(v.context)
        val binding = UpdateDialogItemBinding.inflate(inflater)
        val word = v.tag as Word

        val dialog =AlertDialog.Builder(v.context)
            .setTitle("New Word")
            .setView(binding.root)
            .setPositiveButton("Create", null)
            .create()

        dialog.setOnShowListener{
            binding.edtEnWord.requestFocus()
            showKeyboard(binding.edtEnWord)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enText = binding.edtEnWord.text.toString()
                val ruText = binding.edtRuWord.text.toString()
                val chText = binding.edtChWord.text.toString()
                val chtText = binding.edtChTranscrip.text.toString()

                if (enText.isBlank()){
                    binding.edtEnWord.error = "Value is empty"
                    return@setOnClickListener
                } else if (ruText.isBlank()){
                    binding.edtRuWord.error = "Value is empty"
                    return@setOnClickListener
                } else if (chText.isBlank()){
                    binding.edtChWord.error = "Value is empty"
                    return@setOnClickListener
                } else if (chtText.isBlank()){
                    binding.edtChTranscrip.error = "Value is empty"
                    return@setOnClickListener
                }

                userActionListener.createWord(
                    Word(
                        id = "",
                        enWord = binding.edtEnWord.text.toString().lowercase(),
                        ruWord = binding.edtRuWord.text.toString().lowercase(),
                        chWord = binding.edtChWord.text.toString().lowercase(),
                        chTranscript = binding.edtChTranscrip.text.toString().lowercase()
                    )
                )
                dialog.dismiss()
            }
        }
        dialog.setOnDismissListener { hideKeyboard(binding.edtEnWord) }
        dialog.show()
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

    private fun showDialogForDelete(v: View){
        val listener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> userActionListener.onWordDelete(v.tag as Word)
            }
        }

        val dialog = AlertDialog.Builder(v.context)
            .setTitle("Attention")
            .setMessage("Are you sure?")
            .setCancelable(true)
            .setPositiveButton("Yes", listener)
            .setNegativeButton("No", listener)
            .create()

        dialog.show()
    }

    companion object {
        private const val ID_REMOVE = 1
        private const val ID_REDACT = 2
    }
}


