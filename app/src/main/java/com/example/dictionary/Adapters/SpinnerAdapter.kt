package com.example.dictionary.Adapters

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.dictionary.MainActivity
import com.example.dictionary.R
import com.example.dictionary.databinding.ActivityMainBinding
import java.util.*

class SpinnerAdapter(): AdapterView.OnItemSelectedListener {


    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityMainBinding.inflate(inflater, parent, false)




    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}

//Locale locale = new Locale("en");
//Locale.setDefault(locale);
//Configuration configuration = new Configuration();
//configuration.locale = locale;
//getBaseContext().getResources().updateConfiguration(configuration, null);
// выводим английский текст на русской локали устройства
//setTitle(R.string.app_name);