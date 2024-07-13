package com.cloffygames.myflashcards.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.myflashcards.data.model.CardGroup
import com.cloffygames.myflashcards.data.model.Card
import com.cloffygames.myflashcards.data.repository.CardGroupRepository
import com.cloffygames.myflashcards.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Bu anotasyon, Dagger Hilt'in bu ViewModel'e bağımlılık enjeksiyonu yapmasını sağlar
class HomeViewModel @Inject constructor(
    private val cardGroupRepository: CardGroupRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    // _cardGroupsWithCards, CardGroupWithCards listesini tutan MutableLiveData'dır
    private val _cardGroupsWithCards = MutableLiveData<List<CardGroupWithCards>>()
    // cardGroupsWithCards, _cardGroupsWithCards'i yalnızca okunabilir olarak dışa açar
    val cardGroupsWithCards: LiveData<List<CardGroupWithCards>> get() = _cardGroupsWithCards

    // ViewModel başlatıldığında fetchCardGroupsWithCards fonksiyonunu çağırır
    init {
        fetchCardGroupsWithCards()
    }

    // fetchCardGroupsWithCards, tüm kart gruplarını ve her grubun içindeki kartları çeker
    private fun fetchCardGroupsWithCards() {
        viewModelScope.launch {
            // Tüm kart gruplarını alır
            val cardGroups = cardGroupRepository.getAllCardGroups()
            // Her kart grubu için ilgili kartları alır ve CardGroupWithCards oluşturur
            val cardGroupsWithCards = cardGroups.map { cardGroup ->
                val cards = cardRepository.getCardsByGroupId(cardGroup.groupId)
                CardGroupWithCards(cardGroup, cards)
            }
            // _cardGroupsWithCards'in değerini günceller
            _cardGroupsWithCards.value = cardGroupsWithCards
        }
    }

    // Yeni bir kart grubu ekler ve güncellenmiş kart gruplarını çeker
    fun addCardGroup(cardGroup: CardGroup) {
        viewModelScope.launch {
            cardGroupRepository.insertCardGroup(cardGroup)
            fetchCardGroupsWithCards()
        }
    }
}

// CardGroupWithCards, bir CardGroup ve onun içindeki kartların listesini temsil eder
data class CardGroupWithCards(
    val cardGroup: CardGroup,
    val cards: List<Card>
)