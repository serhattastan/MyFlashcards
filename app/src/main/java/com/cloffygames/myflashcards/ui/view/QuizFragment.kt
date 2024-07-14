package com.cloffygames.myflashcards.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.databinding.FragmentQuizBinding
import com.cloffygames.myflashcards.ui.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint // Bu anotasyon, Dagger Hilt'in bu sınıfa bağımlılık enjeksiyonu yapacağını belirtir
class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by viewModels()
    private val args: QuizFragmentArgs by navArgs()
    private lateinit var cards: List<com.cloffygames.myflashcards.data.model.Card>
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var wrongAnswers = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // FragmentQuizBinding'i inflate eder ve _binding değişkenine atar
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Quiz verilerini yükler
        loadQuizData()

        // backButton'a tıklama olayını dinler ve önceki ekrana döner
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // quiz tamamlandığında homeFragment'a yönlendirir
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_homeFragment)
        }
    }

    // Quiz verilerini yükler ve yeterli terim olup olmadığını kontrol eder
    private fun loadQuizData() {
        CoroutineScope(Dispatchers.Main).launch {
            cards = viewModel.loadCards(args.groupId)
            if (cards.size > 5) {
                displayNextQuestion()
            } else {
                showInsufficientTermsDialog()
            }
        }
    }

    // Sonraki soruyu ekranda gösterir
    private fun displayNextQuestion() {
        if (currentQuestionIndex < cards.size) {
            val card = cards[currentQuestionIndex]
            binding.quizTermTextView.text = card.term

            // Dört rastgele seçenek oluşturur ve doğru cevabı araya serpiştirir
            val options = cards.shuffled().take(4).toMutableList()
            if (!options.contains(card)) {
                options[Random.nextInt(4)] = card
            }

            binding.quizDescTextView.text = options[0].definition
            binding.quizDescTextView2.text = options[1].definition
            binding.quizDescTextView3.text = options[2].definition
            binding.quizDescTextView4.text = options[3].definition

            // Seçenekler için tıklama olaylarını ayarlar
            setOptionsClickListeners(card)
        } else {
            // Tüm sorular tamamlandığında sonuçları gösterir
            showResults()
        }
    }

    // Seçeneklere tıklama olaylarını ayarlar
    private fun setOptionsClickListeners(correctCard: com.cloffygames.myflashcards.data.model.Card) {
        val options = listOf(
            binding.quizDescTextView,
            binding.quizDescTextView2,
            binding.quizDescTextView3,
            binding.quizDescTextView4
        )

        options.forEach { option ->
            option.setOnClickListener {
                if (option.text == correctCard.definition) {
                    correctAnswers++
                } else {
                    wrongAnswers++
                }
                currentQuestionIndex++
                displayNextQuestion()
            }
        }
    }

    // Quiz sonuçlarını ekranda gösterir
    private fun showResults() {
        binding.cardContainer.visibility = View.GONE
        binding.resultCardView.visibility = View.VISIBLE
        binding.questionNumberText.text = "Soru Sayısı: $currentQuestionIndex"
        binding.correctNumberText.text = "Doğru Sayısı: $correctAnswers"
        binding.wrongNumberText.text = "Yanlış Sayısı: $wrongAnswers"
    }

    // Yeterli terim olmadığında uyarı dialogunu gösterir
    private fun showInsufficientTermsDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("The group must contain more than 5 terms to start the quiz. Please add more terms.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}