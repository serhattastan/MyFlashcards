package com.cloffygames.myflashcards.data.repository

import com.cloffygames.myflashcards.data.local.CardGroupDao
import com.cloffygames.myflashcards.data.model.CardGroup
import javax.inject.Inject

class CardGroupRepository @Inject constructor(
    private val cardGroupDao: CardGroupDao
) {
    suspend fun insertCardGroup(cardGroup: CardGroup) {
        cardGroupDao.insertCardGroup(cardGroup)
    }

    suspend fun getAllCardGroups(): List<CardGroup> {
        return cardGroupDao.getAllCardGroups()
    }
}
