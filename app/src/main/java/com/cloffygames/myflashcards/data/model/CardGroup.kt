package com.cloffygames.myflashcards.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CardGroups")
data class CardGroup(
    @PrimaryKey(autoGenerate = true) val groupId: Int,
    val groupName: String
)
