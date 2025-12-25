package com.example.mycloset.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            // מומלץ: Home (אם קיים ב-nav_graph)
            // אם אין לך Home עדיין, החליפי ל-action_login_to_closet
            findNavController().navigate(R.id.action_login_to_home)
        }

        view.findViewById<TextView>(R.id.tvGoRegister).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }
}