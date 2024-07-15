package com.cloffygames.myflashcards.ui.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.data.model.Card
import com.cloffygames.myflashcards.data.model.CardGroup
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
        // FragmentGroupDetailBinding'i inflate eder ve _binding değişkenine atar
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

        // Grup verilerini yükler ve RecyclerView'a bağlar
        loadGroupData()

        // Kart ekleme butonuna tıklama olayını dinler
        binding.addCardButton.setOnClickListener {
            showAddCardDialog()
        }

        // Silme butonuna tıklama olayını dinler
        binding.trashButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Pratik butonuna tıklama olayını dinler
        binding.practiseButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val cards = viewModel.loadCards(args.groupId)
                if (cards.isNotEmpty()) {
                    val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToPractiseFragment(args.groupId)
                    findNavController().navigate(action)
                } else {
                    showEmptyGroupDialog()
                }
            }
        }

        // Quiz butonuna tıklama olayını dinler
        binding.quizButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val cards = viewModel.loadCards(args.groupId)
                if (cards.size > 5) {
                    val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToQuizFragment(args.groupId)
                    findNavController().navigate(action)
                } else {
                    showInsufficientTermsDialog()
                }
            }
        }

        // Grup adı tıklama olayını dinler ve grup adını düzenleme dialogunu gösterir
        binding.groupNameText.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val cardGroup = viewModel.loadCardGroup(args.groupId)
                cardGroup?.let {
                    showEditGroupNameDialog(it)
                }
            }
        }
    }

    // Grup verilerini ve kartları yükler
    private fun loadGroupData() {
        CoroutineScope(Dispatchers.Main).launch {
            val cardGroup = viewModel.loadCardGroup(args.groupId)
            val cards = viewModel.loadCards(args.groupId)

            binding.groupNameText.text = cardGroup?.groupName

            val adapter = CardAdapter(cards, { term ->
                tts.speak(term, TextToSpeech.QUEUE_FLUSH, null, null)
            }) { card ->
                showEditCardDialog(card)
            }
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerView.adapter = adapter
        }
    }

    // Grup adını düzenlemek için dialog gösterir
    private fun showEditGroupNameDialog(cardGroup: CardGroup) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_group_name, null)
        val groupNameEditText = dialogView.findViewById<EditText>(R.id.groupNameEditText)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

        groupNameEditText.setText(cardGroup.groupName)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        saveButton.setOnClickListener {
            val newGroupName = groupNameEditText.text.toString()
            if (newGroupName.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val updatedCardGroup = cardGroup.copy(groupName = newGroupName)
                    viewModel.updateCardGroup(updatedCardGroup)
                    loadGroupData()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    // Kartı düzenlemek için dialog gösterir
    private fun showEditCardDialog(card: Card) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_card, null)
        val termEditText = dialogView.findViewById<EditText>(R.id.termEditText)
        val definitionEditText = dialogView.findViewById<EditText>(R.id.definitionEditText)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

        termEditText.setText(card.term)
        definitionEditText.setText(card.definition)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        saveButton.setOnClickListener {
            val newTerm = termEditText.text.toString()
            val newDefinition = definitionEditText.text.toString()
            if (newTerm.isNotEmpty() && newDefinition.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val updatedCard = card.copy(term = newTerm, definition = newDefinition)
                    viewModel.updateCard(updatedCard)
                    loadGroupData()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    // Yeni bir kart eklemek için dialog gösterir
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
            .setMessage(getString(R.string.delete_confirmation_message))
            .setPositiveButton(getString(R.string.delete_confirmation_positive)) { dialog, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.deleteCardGroup(args.groupId)
                    withContext(Dispatchers.Main) {
                        findNavController().popBackStack()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.delete_confirmation_negative)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Grup boş olduğunda uyarı dialogunu gösterir
    private fun showEmptyGroupDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.empty_group_message))
            .setPositiveButton(getString(R.string.empty_group_positive)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Yetersiz terim olduğunda uyarı dialogunu gösterir
    private fun showInsufficientTermsDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.insufficient_terms_message))
            .setPositiveButton(getString(R.string.empty_group_positive)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Görünüm yok edilirken çağrılır, TTS motorunu kapatır
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tts.shutdown()
    }
}
