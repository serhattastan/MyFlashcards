package com.cloffygames.myflashcards.data.repository

import com.cloffygames.myflashcards.data.local.CardDao
import com.cloffygames.myflashcards.data.model.Card
import javax.inject.Inject

// CardRepository, CardDao'yu kullanarak veritabanı işlemlerini yönetir
class CardRepository @Inject constructor(
    private val cardDao: CardDao
) {
    // Bir Card nesnesini veritabanına ekler
    suspend fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }

    // Belirtilen groupId'ye sahip tüm Card nesnelerini veritabanından alır
    suspend fun getCardsByGroupId(groupId: Int): List<Card> {
        return cardDao.getCardsByGroupId(groupId)
    }
}
