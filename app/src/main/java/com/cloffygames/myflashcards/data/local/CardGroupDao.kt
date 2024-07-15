package com.cloffygames.myflashcards.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cloffygames.myflashcards.data.model.CardGroup

@Dao // Bu anotasyon, bu arayüzün bir Room DAO (Data Access Object) olduğunu belirtir
interface CardGroupDao {

    // Bir CardGroup nesnesini veritabanına ekler
    @Insert
    suspend fun insertCardGroup(cardGroup: CardGroup)

    // Tüm CardGroup nesnelerini veritabanından alır
    @Query("SELECT * FROM CardGroups")
    suspend fun getAllCardGroups(): List<CardGroup>

    // Belirtilen groupId'ye sahip CardGroup nesnesini veritabanından alır
    @Query("SELECT * FROM CardGroups WHERE groupId = :groupId")
    suspend fun getCardGroupById(groupId: Int): CardGroup?

    // Belirtilen CardGroup nesnesini veritabanından siler
    @Delete
    suspend fun deleteCardGroup(cardGroup: CardGroup)

    // Belirtilen CardGroup nesnesini veritabanındaki verileri günceller
    @Update
    suspend fun updateCardGroup(cardGroup: CardGroup)
}
