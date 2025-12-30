package com.example.mycloset.ui.closet

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R

class ClosetListFragment : Fragment(R.layout.fragment_closet_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGoMyItems = view.findViewById<Button>(R.id.btnGoMyItems)
        val btnGoAddItem = view.findViewById<Button>(R.id.btnGoAddItem)

        btnGoMyItems.setOnClickListener {
            findNavController().navigate(R.id.action_closetListFragment_to_itemsListFragment)
        }

        btnGoAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_closetListFragment_to_addItemFragment)
        }
    }
}
