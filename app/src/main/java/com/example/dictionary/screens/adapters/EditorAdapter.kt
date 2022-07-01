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
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.R
import com.example.dictionary.databinding.ItemEditorBinding
import com.example.dictionary.databinding.UpdateDialogItemBinding
import com.example.dictionary.databinding.UpdateEditorDialogBinding
import com.example.dictionary.model.PrivateUser
import com.example.dictionary.model.Word

interface AdminActionListener{
    fun onDeleteEditor(editor: PrivateUser)

    fun onUpdateEditor(editor: PrivateUser)

    fun onCreateEditor(editor: PrivateUser)
}

class EditorAdapter(
    private val actionListener: AdminActionListener
) : RecyclerView.Adapter<EditorAdapter.EditorsViewHolder>(), View.OnClickListener{

    var editorsList: List<PrivateUser> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class EditorsViewHolder (
        val binding: ItemEditorBinding
        ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEditorBinding.inflate(inflater,parent, false)

        binding.btMoreEditor.setOnClickListener(this)

        return EditorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditorsViewHolder, position: Int) {
        val editor = editorsList[position]


        holder.binding.tvEditor.text = editor.login
        holder.binding.btMoreEditor.tag = editor
    }

    override fun getItemCount(): Int = editorsList.size

    override fun onClick(v: View) {
        val editor = v.tag as PrivateUser
        if (v.id == R.id.btMoreEditor) showPopUpMenu(v)
    }

    private fun showPopUpMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        val editor = view.tag as PrivateUser

        popupMenu.menu.add(0, ID_REDACT, Menu.NONE, "Edit")
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Delete")
        popupMenu.menu.add(0, ID_SHOW_PASSWORD, Menu.NONE, "Show password")

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                ID_REDACT ->{
                    showDialogForUpdate(view)
                }
                ID_REMOVE ->{
                    showDialogForDelete(view)
                }
                ID_SHOW_PASSWORD ->{
                    AlertDialog.Builder(view.context)
                        .setCancelable(true)
                        .setTitle("PASSWORD")
                        .setMessage("Password: ${editor.password}")
                        .create()
                        .show()
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    private fun showDialogForDelete(v: View){
        val listener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> actionListener.onDeleteEditor(v.tag as PrivateUser)
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

    private fun showDialogForUpdate(v: View){
        val inflater = LayoutInflater.from(v.context)
        val binding = UpdateEditorDialogBinding.inflate(inflater)
        val editor = v.tag as PrivateUser
        with(binding){
            edtLogin.setText(editor.login)
            edtPassword.setText(editor.password)
        }

        val dialog =AlertDialog.Builder(v.context)
            .setTitle("Edit")
            .setView(binding.root)
            .setPositiveButton("Update", null)
            .create()

        dialog.setOnShowListener{
            binding.edtLogin.requestFocus()
            showKeyboard(binding.edtLogin)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enText = binding.edtLogin.text.toString()
                val ruText = binding.edtPassword.text.toString()

                if (enText.isBlank()){
                    binding.edtLogin.error = "Value is empty"
                    return@setOnClickListener
                } else if (ruText.isBlank()){
                    binding.edtLogin.error = "Value is empty"
                    return@setOnClickListener
                }

                actionListener.onUpdateEditor(
                    PrivateUser(
                        id = editor.id,
                        login = binding.edtLogin.text.toString(),
                        password = binding.edtPassword.text.toString(),
                        role = editor.role
                    )
                )
                dialog.dismiss()
            }
        }
        dialog.setOnDismissListener { hideKeyboard(binding.edtLogin) }
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

    companion object {
        private const val ID_REMOVE = 1
        private const val ID_REDACT = 2
        private const val ID_SHOW_PASSWORD = 3
    }
}