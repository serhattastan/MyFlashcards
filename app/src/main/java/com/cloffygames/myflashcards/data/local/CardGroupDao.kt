package com.cloffygames.myflashcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cloffygames.myflashcards.data.model.CardGroup

@Dao
interface CardGroupDao {
    @Insert
    suspend fun insertCardGroup(cardGroup: CardGroup)

    @Query("SELECT * FROM CardGroups")
    suspend fun getAllCardGroups(): List<CardGroup>
}