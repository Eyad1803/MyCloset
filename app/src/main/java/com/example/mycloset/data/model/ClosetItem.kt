package com.example.mycloset.data.model

/**
 * מייצג פריט בארון (נשמר ב-Firestore).
 */
data class ClosetItem(
    val id: String = "",
    val ownerUid: String = "",
    val closetId: String = "",
    val name: String = "",
    val type: String = "",
    val color: String = "",
    val season: String = "",
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)



