package com.cloffygames.myflashcards.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloffygames.myflashcards.databinding.FragmentHomeBinding
import com.cloffygames.myflashcards.ui.adapter.CardGroupAdapter
import com.cloffygames.myflashcards.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Bu anotasyon, Dagger Hilt'in bu sınıfa bağımlılık enjeksiyonu yapacağını belirtir
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // HomeViewModel'i, Hilt'in viewModels() yardımcı fonksiyonu ile başlatır
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // FragmentHomeBinding'i inflate eder ve _binding değişkenine atar
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel'deki cardGroupsWithCards LiveData'sını gözlemler
        viewModel.cardGroupsWithCards.observe(viewLifecycleOwner) { cardGroupsWithCards ->
            // CardGroupAdapter'ı oluşturur ve RecyclerView'a bağlar
            val adapter = CardGroupAdapter(cardGroupsWithCards)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // _binding'i null yaparak bellek sızıntılarını önler
        _binding = null
    }
}
