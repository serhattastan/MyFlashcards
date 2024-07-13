package com.cloffygames.myflashcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cloffygames.myflashcards.data.model.Card

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: Card)

    @Query("SELECT * FROM Cards WHERE groupId = :groupId")
    suspend fun getCardsByGroupId(groupId: Int): List<Card>
}