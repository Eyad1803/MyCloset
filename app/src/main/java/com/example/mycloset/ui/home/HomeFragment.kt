package com.example.mycloset.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnMenu).setOnClickListener {
            Toast.makeText(requireContext(), "Menu clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnGoCloset).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_closetListFragment)
        }

        view.findViewById<Button>(R.id.btnGoOutfits).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_outfitListFragment)
        }
    }
}