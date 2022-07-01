package com.example.dictionary.screens

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.SingInFragmentBinding
import com.example.dictionary.model.PrivateUser
import com.example.dictionary.retrofit.RetrofitInstance
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

//class SignInFragment: Fragment(R.layout.sing_in_fragment) {
//    private lateinit var binding: SingInFragmentBinding
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = SingInFragmentBinding.bind(view)
//
//        binding.btSignIn.setOnClickListener {
//
////            val login = binding.edLogin.text.toString()
////            val password = binding.edPassword.text.toString()
////
////            if (login.isBlank() or password.isBlank()) return@setOnClickListener
//
//            parentFragmentManager.beginTransaction().replace(R.id.spravkaContainer, SignInFragment()).commit()
////            val job = GlobalScope.launch(Dispatchers.Main) {
////                val response = try {
////                    RetrofitInstance.api.getEditorForLogin(login)
////                } catch (e: IOException){
////                    Log.e("MainAct", "IOException, you might not have internet connection")
////
////                    AlertDialog.Builder(requireContext())
////                        .setCancelable(true)
////                        .setTitle("Error")
////                        .setMessage("The Internet may be turned off, or this is our problem, we will fix it soon")
////                        .create()
////                        .show()
////                    return@launch
////                } catch (e: HttpException){
////                    binding.errorLogin.isVisible = true
////                    return@launch
////                }
////            }
////
////            val respon = response.await()
////
////            if (response.password == password) {
////                val directions = SignInFragmentDirections.actionSignInFragmentToTabsFragment(response.role)
////                findNavController().navigate(directions)
////            } else binding.errorLogin.isVisible = true
//        }
//
//
//    }
//}