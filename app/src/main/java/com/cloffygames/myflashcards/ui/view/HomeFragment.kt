package com.cloffygames.myflashcards.ui.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloffygames.myflashcards.databinding.FragmentHomeBinding
import com.cloffygames.myflashcards.ui.adapter.CardGroupAdapter
import com.cloffygames.myflashcards.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.app.AlertDialog
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.data.model.CardGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint // Bu anotasyon, Dagger Hilt'in bu sınıfa bağımlılık enjeksiyonu yapacağını belirtir
class HomeFragment : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // HomeViewModel'i, Hilt'in viewModels() yardımcı fonksiyonu ile başlatır
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var tts: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // FragmentHomeBinding'i inflate eder ve _binding değişkenine atar
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TextToSpeech (TTS) motorunu başlatır
        tts = TextToSpeech(context, this)
        // addCardGroupButton'a tıklama olayını dinler
        binding.addCardGroupButton.setOnClickListener {
            showAddCardGroupDialog()
        }

        // Sistem geri tuşu basıldığında davranışı özelleştirir
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Fragment yeniden görünür olduğunda kart gruplarını getirir ve gösterir
        fetchAndDisplayCardGroups()
    }

    // Kart gruplarını getirir ve RecyclerView'a bağlar
    private fun fetchAndDisplayCardGroups() {
        CoroutineScope(Dispatchers.Main).launch {
            val cardGroupsWithCards = viewModel.fetchCardGroupsWithCards()
            if (cardGroupsWithCards.isEmpty()) {
                binding.noCardGroupsTextView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.noCardGroupsTextView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                val adapter = CardGroupAdapter(cardGroupsWithCards, { cardGroup ->
                    // Grup detaylarına gitmek için navigasyon işlemini başlatır
                    val action = HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(cardGroup.cardGroup.groupId)
                    findNavController().navigate(action)
                }, { term ->
                    // TTS ile terimi okur
                    tts.speak(term, TextToSpeech.QUEUE_FLUSH, null, null)
                })
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                binding.recyclerView.adapter = adapter
            }
        }
    }

    // Yeni bir kart grubu eklemek için bir dialog gösterir
    private fun showAddCardGroupDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_card_group, null)
        val groupNameEditText = dialogView.findViewById<EditText>(R.id.groupNameEditText)
        val addButton = dialogView.findViewById<Button>(R.id.addButton)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        addButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString()
            if (groupName.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.addCardGroup(CardGroup(groupName = groupName))
                    fetchAndDisplayCardGroups()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    // Uygulamayı kapatmak için onay isteyen bir dialog gösterir
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.exit_confirmation_message)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                requireActivity().finish()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // TTS motoru başlatıldığında çağrılır
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", getString(R.string.language_not_supported))
            } else {
                Log.d("TTS", getString(R.string.tts_initialized_successfully))
            }
        } else {
            Log.e("TTS", getString(R.string.tts_initialization_failed))
        }
    }

    // Görünüm yok edilirken çağrılır, TTS motorunu kapatır
    override fun onDestroyView() {
        super.onDestroyView()
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        _binding = null
    }
}
