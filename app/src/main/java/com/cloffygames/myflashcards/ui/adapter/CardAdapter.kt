package com.cloffygames.myflashcards.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloffygames.myflashcards.databinding.ItemCardBinding
import com.cloffygames.myflashcards.data.model.Card

class CardAdapter(
    private val cards: List<Card>,
    private val onSoundIconClicked: (String) -> Unit,
    private val onItemClicked: (Card) -> Unit // Kart tıklama dinleyicisi
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    // ViewHolder sınıfı, her bir kart öğesi için arayüz bileşenlerini tutar
    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        // Kart öğesini arayüz bileşenlerine bağlar
        fun bind(card: Card) {
            binding.termTextView.text = card.term
            binding.definitionTextView.text = card.definition

            // Ses ikonuna tıklama olayını dinler ve onSoundIconClicked fonksiyonunu çağırır
            binding.soundIcon.setOnClickListener {
                onSoundIconClicked(card.term)
            }

            // Kart tıklama olayını dinler ve onItemClicked fonksiyonunu çağırır
            binding.root.setOnClickListener {
                onItemClicked(card)
            }
        }
    }

    // ViewHolder'ı oluşturur ve ilgili arayüz bileşenlerini inflate eder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    // Verilen pozisyondaki kartı ViewHolder'a bağlar
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    // Toplam kart sayısını döndürür
    override fun getItemCount(): Int = cards.size
}
