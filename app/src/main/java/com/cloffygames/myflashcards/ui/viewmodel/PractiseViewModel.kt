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
class PractiseViewModel @Inject constructor(
    private val cardGroupRepository: CardGroupRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    // Belirtilen groupId'ye sahip CardGroup nesnesini veritabanından alır
    suspend fun loadCardGroup(groupId: Int): CardGroup? {
        return withContext(Dispatchers.IO) {
            cardGroupRepository.getCardGroupById(groupId)
        }
    }

    // Belirtilen groupId'ye sahip tüm Card nesnelerini veritabanından alır
    suspend fun loadCards(groupId: Int): List<Card> {
        return withContext(Dispatchers.IO) {
            cardRepository.getCardsByGroupId(groupId)
        }
    }
}
