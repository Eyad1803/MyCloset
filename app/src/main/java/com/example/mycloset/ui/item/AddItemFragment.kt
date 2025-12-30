package com.example.mycloset.ui.item

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mycloset.R
import com.example.mycloset.data.model.ClosetItem
import com.example.mycloset.data.repository.ItemsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.File

class AddItemFragment : Fragment(R.layout.fragment_add_item) {

    private val repo = ItemsRepository()
    private var selectedImageUri: Uri? = null

    // ✅ גלריה
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                view?.findViewById<ImageView>(R.id.imgItem)?.setImageURI(uri)
            }
        }

    // ✅ מצלמה (שומר לקובץ ואז יש Uri אמיתי ל-Storage)
    private var cameraTempUri: Uri? = null
    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                selectedImageUri = cameraTempUri
                view?.findViewById<ImageView>(R.id.imgItem)?.setImageURI(cameraTempUri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_global_loginFragment)
            return
        }

        val imgItem = view.findViewById<ImageView>(R.id.imgItem)
        val btnTakePhoto = view.findViewById<Button>(R.id.btnTakePhoto)
        val btnPickImage = view.findViewById<Button>(R.id.btnPickImage)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val progress = view.findViewById<ProgressBar>(R.id.progress)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etType = view.findViewById<EditText>(R.id.etType)
        val etColor = view.findViewById<EditText>(R.id.etColor)
        val etSeason = view.findViewById<EditText>(R.id.etSeason)
        val etTags = view.findViewById<EditText>(R.id.etTags)

        btnPickImage.setOnClickListener { pickImageLauncher.launch("image/*") }

        btnTakePhoto.setOnClickListener {
            cameraTempUri = createTempImageUri()
            takePhotoLauncher.launch(cameraTempUri)
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val type = etType.text.toString().trim()
            val color = etColor.text.toString().trim()
            val season = etSeason.text.toString().trim()
            val tags = etTags.text.toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (name.isEmpty() || type.isEmpty()) {
                Toast.makeText(requireContext(), "Name ו-Type חובה", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    progress.visibility = View.VISIBLE
                    btnSave.isEnabled = false
                    btnPickImage.isEnabled = false
                    btnTakePhoto.isEnabled = false

                    val imageUrl = selectedImageUri?.let { uri ->
                        repo.uploadImage(userId, uri)
                    } ?: ""

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

                    repo.addItem(userId, item)
                    Toast.makeText(requireContext(), "Item נשמר בהצלחה ✅", Toast.LENGTH_SHORT).show()

                    // ✅ חוזרים לרשימת My Items (להצגה!)
                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "שגיאה: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    progress.visibility = View.GONE
                    btnSave.isEnabled = true
                    btnPickImage.isEnabled = true
                    btnTakePhoto.isEnabled = true
                }
            }
        }
    }

    private fun createTempImageUri(): Uri {
        val imagesDir = File(requireContext().cacheDir, "images").apply { mkdirs() }
        val imageFile = File(imagesDir, "camera_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
    }
}
