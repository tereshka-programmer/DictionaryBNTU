package com.example.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dictionary.databinding.ActivitySpravkaBinding
import com.example.dictionary.screens.SpravkaFragment
import com.example.dictionary.screens.TabsFragment

class Spravka : AppCompatActivity() {
    private lateinit var binding: ActivitySpravkaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpravkaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.spravkaContainer, SpravkaFragment())
            .commit()
    }
}