package com.example.mycloset.data.remote

import android.net.Uri
import com.example.mycloset.data.model.ClosetItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseItemsDataSource(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    suspend fun uploadItemImage(userId: String, localUri: Uri): String {
        val ref = storage.reference
            .child("users/$userId/items/${System.currentTimeMillis()}.jpg")

        ref.putFile(localUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun addItem(userId: String, item: ClosetItem): String {
        val doc = db.collection("users")
            .document(userId)
            .collection("items")
            .document()

        val itemWithId = item.copy(id = doc.id)
        doc.set(itemWithId).await()
        return doc.id
    }


    suspend fun getItemsByOwner(userId: String): List<ClosetItem> {
        val snapshot = db.collection("users")
            .document(userId)
            .collection("items")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(ClosetItem::class.java) }
    }



}





