package com.example.dictionary.screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.App
import com.example.dictionary.R
import com.example.dictionary.databinding.EditorsFragmentBinding
import com.example.dictionary.databinding.UpdateDialogItemBinding
import com.example.dictionary.databinding.UpdateEditorDialogBinding
import com.example.dictionary.model.PrivateUser
import com.example.dictionary.model.Word
import com.example.dictionary.screens.adapters.AdminActionListener
import com.example.dictionary.screens.adapters.EditorAdapter
import com.example.dictionary.screens.services.EditorServiceListener
import com.example.dictionary.screens.services.EditorsService

class EditorsFragment : Fragment(R.layout.editors_fragment) {

    private lateinit var binding: EditorsFragmentBinding
    private lateinit var adapter: EditorAdapter

    private val editorsService: EditorsService
        get() = (activity?.applicationContext as App).editorService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditorsFragmentBinding.bind(view)

        adapter = EditorAdapter(object : AdminActionListener{
            override fun onDeleteEditor(editor: PrivateUser) {
                editorsService.deleteEditor(editor)
            }

            override fun onUpdateEditor(editor: PrivateUser) {
                editorsService.updateEditor(editor)
            }

            override fun onCreateEditor(editor: PrivateUser) {
                editorsService.createEditor(editor)
            }

        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rcvEditors.adapter = adapter
        binding.rcvEditors.layoutManager = layoutManager

        editorsService.addListener(editorListener)

        binding.btAdd.setOnClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val itemBinding = UpdateEditorDialogBinding.inflate(inflater)

            val listener = DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE){
                    editorsService.createEditor( PrivateUser(
                        id = null,
                        login = itemBinding.edtLogin.text.toString(),
                        password = itemBinding.edtPassword.text.toString(),
                        role = "editor"
                    ))
                }
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("New Editor")
                .setView(itemBinding.root)
                .setPositiveButton("Create", listener)
                .create()

            dialog.show()
        }

    }

    private val editorListener: EditorServiceListener = {
        adapter.editorsList = it
    }

    override fun onDestroy() {
        super.onDestroy()
        editorsService.removeListener(editorListener)
    }
}