package com.cloffygames.myflashcards.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloffygames.myflashcards.databinding.ItemCardGroupBinding
import com.cloffygames.myflashcards.ui.viewmodel.CardGroupWithCards

// CardGroupAdapter, RecyclerView.Adapter sınıfını genişletir ve kart gruplarını listeler
class CardGroupAdapter(
    private val cardGroupsWithCards: List<CardGroupWithCards>,
    private val onItemClicked: (CardGroupWithCards) -> Unit,
    private val onSoundIconClicked: (String) -> Unit
) : RecyclerView.Adapter<CardGroupAdapter.CardGroupViewHolder>() {

    // CardGroupViewHolder, RecyclerView.ViewHolder sınıfını genişletir ve item_card_group layout'unun verilerini bağlar
    inner class CardGroupViewHolder(private val binding: ItemCardGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        // bind metodu, CardGroupWithCards modelinden gelen verileri view'lara bağlar
        fun bind(cardGroupWithCards: CardGroupWithCards) {
            binding.groupNameTextView.text = cardGroupWithCards.cardGroup.groupName

            // CardAdapter'ı oluşturur ve RecyclerView'a yatay olarak bağlar
            val cardAdapter = CardAdapter(cardGroupWithCards.cards, onSoundIconClicked)
            binding.cardsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.cardsRecyclerView.adapter = cardAdapter

            // Grup kartının tıklama olayını dinler ve onItemClicked fonksiyonunu çağırır
            binding.root.setOnClickListener {
                onItemClicked(cardGroupWithCards)
            }
        }
    }

    // onCreateViewHolder metodu, ViewHolder'ı oluşturur ve layout'u inflate eder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardGroupViewHolder {
        val binding = ItemCardGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardGroupViewHolder(binding)
    }

    // onBindViewHolder metodu, ViewHolder'a verileri bağlar
    override fun onBindViewHolder(holder: CardGroupViewHolder, position: Int) {
        holder.bind(cardGroupsWithCards[position])
    }

    // getItemCount metodu, liste içerisindeki item sayısını döner
    override fun getItemCount(): Int = cardGroupsWithCards.size
}
