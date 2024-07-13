package com.cloffygames.myflashcards.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val cardId: Int,
    val term: String,
    val definition: String,
    val groupId: Int
)
