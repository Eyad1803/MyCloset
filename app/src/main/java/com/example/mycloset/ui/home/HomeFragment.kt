package com.example.mycloset.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGoClosets = view.findViewById<Button>(R.id.btnGoClosets)
        val btnGoOutfits = view.findViewById<Button>(R.id.btnGoOutfits)
        val btnAddItem = view.findViewById<Button>(R.id.btnAddItem)
        val btnMyItems = view.findViewById<Button>(R.id.btnMyItems)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        btnGoClosets.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_closetListFragment)
        }

        btnGoOutfits.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_outfitListFragment)
        }

        btnAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addItemFragment)
        }

        btnMyItems.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_itemsListFragment)
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_global_loginFragment)
        }
    }
}
