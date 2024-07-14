package com.cloffygames.myflashcards.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.databinding.FragmentPractiseBinding
import com.cloffygames.myflashcards.ui.viewmodel.PractiseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint // Bu anotasyon, Dagger Hilt'in bu sınıfa bağımlılık enjeksiyonu yapacağını belirtir
class PractiseFragment : Fragment(), TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private var _binding: FragmentPractiseBinding? = null
    private val binding get() = _binding!!

    // PractiseViewModel'i, Hilt'in viewModels() yardımcı fonksiyonu ile başlatır
    private val viewModel: PractiseViewModel by viewModels()
    private val args: PractiseFragmentArgs by navArgs()
    private lateinit var tts: TextToSpeech
    private lateinit var cards: List<com.cloffygames.myflashcards.data.model.Card>
    private var currentIndex = 0
    private var originalColor: Int = 0
    private var currentlySpeakingTextView: TextView? = null

    // Kartların sırasını tutan liste
    private val cardSequence = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // FragmentPractiseBinding'i inflate eder ve _binding değişkenine atar
        _binding = FragmentPractiseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TextToSpeech (TTS) motorunu başlatır
        tts = TextToSpeech(context, this)

        // Grup verilerini yükler ve gösterir
        loadGroupData()

        // Ses ikonuna tıklama olayını dinler ve metni seslendirir
        binding.soundImage.setOnClickListener {
            speakText(binding.practiseTermText, Locale.getDefault())
        }

        // Sol ok ikonuna tıklama olayını dinler ve önceki kartı gösterir
        binding.leftButtonImage.setOnClickListener {
            showPreviousCard()
        }

        // Sağ ok ikonuna tıklama olayını dinler ve sonraki kartı gösterir
        binding.rightButtonImage.setOnClickListener {
            showNextCard()
        }

        // Geri butonuna tıklama olayını dinler ve önceki ekrana döner
        binding.backGroupDetailButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // Grup verilerini ve kartları yükler
    private fun loadGroupData() {
        CoroutineScope(Dispatchers.Main).launch {
            val cardGroup = viewModel.loadCardGroup(args.groupId)
            binding.practiseGroupNameText.text = cardGroup?.groupName

            cards = viewModel.loadCards(args.groupId)
            if (cards.isNotEmpty()) {
                cardSequence.clear()
                cardSequence.addAll(cards.indices.shuffled())
                currentIndex = 0
                displayCard()
            }
        }
    }

    // Sonraki kartı gösterir
    private fun showNextCard() {
        currentIndex = (currentIndex + 1) % cardSequence.size
        displayCard()
    }

    // Önceki kartı gösterir
    private fun showPreviousCard() {
        currentIndex = if (currentIndex - 1 < 0) cardSequence.size - 1 else currentIndex - 1
        displayCard()
    }

    // Şu anki kartı ekranda gösterir
    private fun displayCard() {
        val cardIndex = cardSequence[currentIndex]
        binding.practiseTermText.text = cards[cardIndex].term
        binding.practiseDescriptionText.text = cards[cardIndex].definition
        binding.practiseTermText.setTextColor(resources.getColor(R.color.secondary_color))
    }

    // Belirtilen TextView'in metnini seslendirir ve metin rengini değiştirir
    private fun speakText(textView: TextView, locale: Locale) {
        stopSpeakingIfNeeded()
        tts.language = locale
        val textToRead = textView.text.toString()
        currentlySpeakingTextView = textView
        originalColor = textView.currentTextColor
        textView.setTextColor(resources.getColor(R.color.alert_color))
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textToRead)
        tts.speak(textToRead, TextToSpeech.QUEUE_FLUSH, params, textToRead)
    }

    // TTS konuşuyorsa durdurur ve metin rengini eski haline getirir
    private fun stopSpeakingIfNeeded() {
        if (tts.isSpeaking) {
            tts.stop()
            currentlySpeakingTextView?.let {
                it.setTextColor(originalColor)
                currentlySpeakingTextView = null
            }
        }
    }

    // TTS motoru başlatıldığında çağrılır
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
            tts.setOnUtteranceCompletedListener(this)
        }
    }

    // TTS konuşması tamamlandığında çağrılır
    override fun onUtteranceCompleted(utteranceId: String?) {
        activity?.runOnUiThread {
            currentlySpeakingTextView?.let {
                it.setTextColor(originalColor)
                currentlySpeakingTextView = null
            }
        }
    }

    // Görünüm yok edilirken çağrılır, TTS motorunu kapatır
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}
