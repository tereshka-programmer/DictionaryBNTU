package com.example.dictionary.screens

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.R
import com.example.dictionary.databinding.SingInFragmentBinding
import com.example.dictionary.databinding.SpravkaFragmentBinding
import com.example.dictionary.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SpravkaFragment : Fragment(R.layout.sing_in_fragment){

    lateinit var binding: SingInFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingInFragmentBinding.bind(view)

        binding.btSignIn.setOnClickListener {
            val login = binding.edLogin.text.toString()
            val password = binding.edPassword.text.toString()

            if (login.isBlank() or password.isBlank()) return@setOnClickListener

            lifecycleScope.launchWhenCreated {

                val response = try {
                    RetrofitInstance.api.getEditorForLogin(login)
                } catch (e: IOException){
                    AlertDialog.Builder(requireContext())
                        .setCancelable(true)
                        .setTitle("Error")
                        .setMessage("The Internet may be turned off, or this is our problem, we will fix it soon")
                        .create()
                        .show()
                    return@launchWhenCreated
                } catch (e: HttpException){
                    binding.errorLogin.isVisible = true
                    return@launchWhenCreated
                }

                if (response.password == password){
                    val bundle = Bundle()
                    bundle.putString("data", response.role)

                    val frag = TabsFragment()
                    frag.arguments = bundle

                    parentFragmentManager.beginTransaction().replace(R.id.spravkaContainer, frag).commit()
                } else binding.errorLogin.isVisible = true
            }
        }
    }


}