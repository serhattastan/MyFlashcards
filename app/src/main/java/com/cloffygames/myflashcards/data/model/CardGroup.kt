package com.cloffygames.myflashcards.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// CardGroups tablosunu temsil eden CardGroup veri sınıfı
@Entity(tableName = "CardGroups")
data class CardGroup(
    @PrimaryKey(autoGenerate = true) val groupId: Int = 0, // Her grup için otomatik olarak oluşturulan birincil anahtar
    val groupName: String // Grubun adı
)
