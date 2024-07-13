package com.cloffygames.myflashcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cloffygames.myflashcards.data.model.Card

@Dao // Bu anotasyon, bu arayüzün bir Room DAO (Data Access Object) olduğunu belirtir
interface CardDao {

    // Bir Card nesnesini veritabanına ekler
    @Insert
    suspend fun insertCard(card: Card)

    // Belirtilen groupId'ye sahip tüm Card nesnelerini döner
    @Query("SELECT * FROM Cards WHERE groupId = :groupId")
    suspend fun getCardsByGroupId(groupId: Int): List<Card>
}
