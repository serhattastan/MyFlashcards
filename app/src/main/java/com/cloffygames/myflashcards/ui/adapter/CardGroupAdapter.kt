package com.cloffygames.myflashcards.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloffygames.myflashcards.databinding.ItemCardGroupBinding
import com.cloffygames.myflashcards.ui.viewmodel.CardGroupWithCards

class CardGroupAdapter(
    private val cardGroupsWithCards: List<CardGroupWithCards>,
    private val onItemClicked: (CardGroupWithCards) -> Unit,
    private val onSoundIconClicked: (String) -> Unit,
    private val onGroupNameClicked: (CardGroupWithCards) -> Unit // Grup adı tıklama dinleyicisi
) : RecyclerView.Adapter<CardGroupAdapter.CardGroupViewHolder>() {

    // ViewHolder sınıfı, her bir kart grubu öğesi için arayüz bileşenlerini tutar
    inner class CardGroupViewHolder(private val binding: ItemCardGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        // Kart grubu öğesini arayüz bileşenlerine bağlar
        fun bind(cardGroupWithCards: CardGroupWithCards) {
            binding.groupNameTextView.text = cardGroupWithCards.cardGroup.groupName

            // İç içe kartlar için CardAdapter oluşturur
            val cardAdapter = CardAdapter(cardGroupWithCards.cards, onSoundIconClicked) { card ->
                onItemClicked(cardGroupWithCards)
            }
            binding.cardsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.cardsRecyclerView.adapter = cardAdapter

            // Grup adı tıklama olayını dinler ve onGroupNameClicked fonksiyonunu çağırır
            binding.groupNameTextView.setOnClickListener {
                onGroupNameClicked(cardGroupWithCards)
            }

            // Grup kartının tıklama olayını dinler ve onItemClicked fonksiyonunu çağırır
            binding.root.setOnClickListener {
                onItemClicked(cardGroupWithCards)
            }
        }
    }

    // ViewHolder'ı oluşturur ve ilgili arayüz bileşenlerini inflate eder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardGroupViewHolder {
        val binding = ItemCardGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardGroupViewHolder(binding)
    }

    // Verilen pozisyondaki kart grubunu ViewHolder'a bağlar
    override fun onBindViewHolder(holder: CardGroupViewHolder, position: Int) {
        holder.bind(cardGroupsWithCards[position])
    }

    // Toplam kart grubu sayısını döndürür
    override fun getItemCount(): Int = cardGroupsWithCards.size
}
