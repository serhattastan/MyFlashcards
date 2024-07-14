package com.cloffygames.myflashcards.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.myflashcards.data.model.Card
import com.cloffygames.myflashcards.data.model.CardGroup
import com.cloffygames.myflashcards.data.repository.CardGroupRepository
import com.cloffygames.myflashcards.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel // Bu anotasyon, Dagger Hilt'in bu ViewModel'e bağımlılık enjeksiyonu yapmasını sağlar
class HomeViewModel @Inject constructor(
    private val cardGroupRepository: CardGroupRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    // Tüm kart gruplarını ve her grubun içindeki kartları veritabanından getirir
    suspend fun fetchCardGroupsWithCards(): List<CardGroupWithCards> {
        return withContext(Dispatchers.IO) {
            val cardGroups = cardGroupRepository.getAllCardGroups()
            cardGroups.map { cardGroup ->
                val cards = cardRepository.getCardsByGroupId(cardGroup.groupId)
                CardGroupWithCards(cardGroup, cards)
            }
        }
    }

    // Yeni bir kart grubu ekler
    suspend fun addCardGroup(cardGroup: CardGroup) {
        withContext(Dispatchers.IO) {
            cardGroupRepository.insertCardGroup(cardGroup)
        }
    }
}

// CardGroupWithCards, bir CardGroup ve onun içindeki kartların listesini temsil eder
data class CardGroupWithCards(
    val cardGroup: CardGroup,
    val cards: List<Card>
)
