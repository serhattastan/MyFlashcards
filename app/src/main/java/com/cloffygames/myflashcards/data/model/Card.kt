package com.cloffygames.myflashcards.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Cards tablosunu temsil eden Card veri sınıfı
@Entity(tableName = "Cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val cardId: Int, // Her kart için otomatik olarak oluşturulan birincil anahtar
    val term: String, // Kartın terimi
    val definition: String, // Kartın tanımı
    val groupId: Int // Kartın ait olduğu grubun kimliği
)
