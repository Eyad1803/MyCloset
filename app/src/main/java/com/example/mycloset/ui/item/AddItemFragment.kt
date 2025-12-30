package com.example.mycloset.ui.item

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R
import com.example.mycloset.data.model.ClosetItem
import com.example.mycloset.data.repository.ItemsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddItemFragment : Fragment(R.layout.fragment_add_item) {

    private val repo = ItemsRepository()
    private var selectedImageUri: Uri? = null

    // בחירת תמונה מהגלריה
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                view?.findViewById<ImageView>(R.id.imgItem)?.setImageURI(uri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // בדיקת משתמש מחובר
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_global_loginFragment)
            return
        }

        // חיבור רכיבי UI
        val imgItem = view.findViewById<ImageView>(R.id.imgItem)
        val btnPickImage = view.findViewById<Button>(R.id.btnPickImage)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val progress = view.findViewById<ProgressBar>(R.id.progress)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etType = view.findViewById<EditText>(R.id.etType)
        val etColor = view.findViewById<EditText>(R.id.etColor)
        val etSeason = view.findViewById<EditText>(R.id.etSeason)
        val etTags = view.findViewById<EditText>(R.id.etTags)

        // בחירת תמונה
        btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // שמירת פריט
        btnSave.setOnClickListener {

            val name = etName.text.toString().trim()
            val type = etType.text.toString().trim()
            val color = etColor.text.toString().trim()
            val season = etSeason.text.toString().trim()
            val tags = etTags.text.toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            // ולידציה בסיסית
            if (name.isEmpty() || type.isEmpty()) {
                Toast.makeText(requireContext(), "Name ו-Type הם שדות חובה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    progress.visibility = View.VISIBLE
                    btnSave.isEnabled = false
                    btnPickImage.isEnabled = false

                    // העלאת תמונה (אם קיימת)
                    val imageUrl = selectedImageUri?.let { uri ->
                        repo.uploadImage(userId, uri)
                    } ?: ""

                    // יצירת אובייקט פריט
                    val item = ClosetItem(
                        ownerUid = userId,
                        closetId = "default",
                        name = name,
                        type = type,
                        color = color,
                        season = season,
                        tags = tags,
                        imageUrl = imageUrl
                    )

                    // 🔥 שמירה ב-Firestore
                    repo.addItem(userId, item)

                    Toast.makeText(
                        requireContext(),
                        "Item נשמר בהצלחה ✅",
                        Toast.LENGTH_SHORT
                    ).show()

                    // ✅ חזרה למסך My Items (הצגת אמצע!)
                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "שגיאה: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    progress.visibility = View.GONE
                    btnSave.isEnabled = true
                    btnPickImage.isEnabled = true
                }
            }
        }
    }
}
