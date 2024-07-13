package com.cloffygames.myflashcards.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloffygames.myflashcards.databinding.ItemCardBinding
import com.cloffygames.myflashcards.data.model.Card

// CardAdapter sınıfı, RecyclerView.Adapter sınıfını genişletir ve flashcard'ları listeler
class CardAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    // CardViewHolder, RecyclerView.ViewHolder sınıfını genişletir ve item_card layout'unun verilerini bağlar
    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        // bind metodu, Card modelinden gelen verileri view'lara bağlar
        fun bind(card: Card) {
            binding.termTextView.text = card.term
            binding.definitionTextView.text = card.definition
        }
    }

    // onCreateViewHolder metodu, ViewHolder'ı oluşturur ve layout'u inflate eder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    // onBindViewHolder metodu, ViewHolder'a verileri bağlar
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    // getItemCount metodu, liste içerisindeki item sayısını döner
    override fun getItemCount(): Int = cards.size
}
