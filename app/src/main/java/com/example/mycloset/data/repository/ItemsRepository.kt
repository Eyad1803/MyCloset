package com.example.mycloset.data.repository

import android.net.Uri
import com.example.mycloset.data.model.ClosetItem
import com.example.mycloset.data.remote.FirebaseItemsDataSource

class ItemsRepository(
    private val dataSource: FirebaseItemsDataSource = FirebaseItemsDataSource()
) {
    suspend fun uploadImage(userId: String, uri: Uri): String {
        return dataSource.uploadItemImage(userId, uri)
    }

    suspend fun addItem(userId: String, item: ClosetItem): String {
        return dataSource.addItem(userId, item)
    }

    // ✅ זה היה חסר אצלך
    suspend fun getMyItems(userId: String): List<ClosetItem> {
        return dataSource.getItemsByOwner(userId)
    }
}

