package com.example.docexpiry.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room entity representing a government card
@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val number: String,
    val ownerName: String,
    val ownerAddress: String? = null,
    val ownerDob: Long = 0L,
    val authority: String,
    val issuedDate: Long,
    val expiryDate: Long,
    val photoUri: String?
)
