package com.cloffygames.myflashcards.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.myflashcards.data.model.Card
import com.cloffygames.myflashcards.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel // Bu anotasyon, Dagger Hilt'in bu ViewModel'e bağımlılık enjeksiyonu yapmasını sağlar
class QuizViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    // Belirtilen groupId'ye sahip tüm Card nesnelerini veritabanından alır
    suspend fun loadCards(groupId: Int): List<Card> {
        return withContext(Dispatchers.IO) {
            cardRepository.getCardsByGroupId(groupId)
        }
    }
}
