package com.cloffygames.myflashcards.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.cloffygames.myflashcards.databinding.FragmentGroupDetailBinding
import com.cloffygames.myflashcards.ui.adapter.CardAdapter
import com.cloffygames.myflashcards.ui.viewmodel.GroupDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.speech.tts.TextToSpeech
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.data.model.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@AndroidEntryPoint // Bu anotasyon, Dagger Hilt'in bu sınıfa bağımlılık enjeksiyonu yapacağını belirtir
class GroupDetailFragment : Fragment() {

    private var _binding: FragmentGroupDetailBinding? = null
    private val binding get() = _binding!!

    // GroupDetailViewModel'i, Hilt'in viewModels() yardımcı fonksiyonu ile başlatır
    private val viewModel: GroupDetailViewModel by viewModels()
    private val args: GroupDetailFragmentArgs by navArgs()
    private lateinit var tts: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TextToSpeech (TTS) motorunu başlatır
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.getDefault()
            }
        }

        loadGroupData()

        // addCardButton'a tıklama olayını dinler
        binding.addCardButton.setOnClickListener {
            showAddCardDialog()
        }

        // trashButton'a tıklama olayını dinler
        binding.trashButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    // Grup verilerini yükler ve RecyclerView'a bağlar
    private fun loadGroupData() {
        CoroutineScope(Dispatchers.Main).launch {
            val cardGroup = viewModel.loadCardGroup(args.groupId)
            val cards = viewModel.loadCards(args.groupId)

            binding.groupNameText.text = cardGroup?.groupName

            val adapter = CardAdapter(cards) { term ->
                tts.speak(term, TextToSpeech.QUEUE_FLUSH, null, null)
            }
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerView.adapter = adapter
        }
    }

    // Yeni bir kart eklemek için bir dialog gösterir
    private fun showAddCardDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_card, null)
        val termEditText = dialogView.findViewById<EditText>(R.id.termEditText)
        val definitionEditText = dialogView.findViewById<EditText>(R.id.definitionEditText)
        val addButton = dialogView.findViewById<Button>(R.id.addButton)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        addButton.setOnClickListener {
            val term = termEditText.text.toString()
            val definition = definitionEditText.text.toString()
            if (term.isNotEmpty() && definition.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.addCard(Card(term = term, definition = definition, groupId = args.groupId))
                    loadGroupData()
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    // Silme onay dialogunu gösterir
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this group and all its cards?")
            .setPositiveButton("Yes") { dialog, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.deleteCardGroup(args.groupId)
                    withContext(Dispatchers.Main) {
                        findNavController().popBackStack()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // TextToSpeech (TTS) motorunu kapatır
        tts.shutdown()
    }
}