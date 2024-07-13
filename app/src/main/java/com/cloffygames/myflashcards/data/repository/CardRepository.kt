package com.cloffygames.myflashcards.data.repository

import com.cloffygames.myflashcards.data.local.CardDao
import com.cloffygames.myflashcards.data.model.Card
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardDao: CardDao
) {
    suspend fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }

    suspend fun getCardsByGroupId(groupId: Int): List<Card> {
        return cardDao.getCardsByGroupId(groupId)
    }
}
