package com.cloffygames.myflashcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cloffygames.myflashcards.data.model.CardGroup

@Dao // Bu anotasyon, bu arayüzün bir Room DAO (Data Access Object) olduğunu belirtir
interface CardGroupDao {

    // Bir CardGroup nesnesini veritabanına ekler
    @Insert
    suspend fun insertCardGroup(cardGroup: CardGroup)

    // Tüm CardGroup nesnelerini döner
    @Query("SELECT * FROM CardGroups")
    suspend fun getAllCardGroups(): List<CardGroup>
}
