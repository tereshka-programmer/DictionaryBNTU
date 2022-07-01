package com.example.dictionary.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dictionary.R
import com.example.dictionary.databinding.TabsFragmentBinding

class TabsFragment : Fragment(R.layout.tabs_fragment) {

    private lateinit var binding: TabsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TabsFragmentBinding.bind(view)

        val data = requireArguments().getString("data")
        if (data == "editor") binding.bottomNavigationView.inflateMenu(R.menu.tabs_menu_for_editor)
        if (data == "admin") binding.bottomNavigationView.inflateMenu(R.menu.tabs_menu)


        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }
}