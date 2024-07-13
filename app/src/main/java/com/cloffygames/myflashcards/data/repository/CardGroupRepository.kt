package com.cloffygames.myflashcards.data.repository

import com.cloffygames.myflashcards.data.local.CardGroupDao
import com.cloffygames.myflashcards.data.model.CardGroup
import javax.inject.Inject

// CardGroupRepository, CardGroupDao'yu kullanarak veritabanı işlemlerini yönetir
class CardGroupRepository @Inject constructor(
    private val cardGroupDao: CardGroupDao
) {
    // Bir CardGroup nesnesini veritabanına ekler
    suspend fun insertCardGroup(cardGroup: CardGroup) {
        cardGroupDao.insertCardGroup(cardGroup)
    }

    // Tüm CardGroup nesnelerini veritabanından alır
    suspend fun getAllCardGroups(): List<CardGroup> {
        return cardGroupDao.getAllCardGroups()
    }
}
