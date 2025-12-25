package com.example.mycloset.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            // מומלץ: Home (אם קיים ב-nav_graph)
            // אם אין לך Home עדיין, החליפי ל-action_register_to_closet
            findNavController().navigate(R.id.action_register_to_home)
        }

        view.findViewById<TextView>(R.id.tvGoLogin).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}