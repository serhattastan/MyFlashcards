package com.cloffygames.myflashcards.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.data.repository.PreferencesRepository
import com.cloffygames.myflashcards.databinding.FragmentSettingsBinding
import com.cloffygames.myflashcards.ui.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Night mode switch ayarı ve dinleyicisi
        binding.switchNightMode.isChecked = preferencesRepository.isDarkModeEnabled()
        binding.switchNightMode.setOnCheckedChangeListener { _, isChecked ->
            preferencesRepository.setDarkMode(isChecked)
            applyTheme(isChecked)
        }

        // Sosyal medya butonlarına tıklama olayları
        binding.linkedInLogoView.setOnClickListener {
            openUrl("https://www.linkedin.com/company/cloffy-games")
        }

        binding.instagramLogoView.setOnClickListener {
            openUrl("https://www.instagram.com/cloffygames")
        }

        binding.facebookLogoView.setOnClickListener {
            openUrl("https://www.facebook.com/profile.php?id=61550253343078")
        }

        binding.xLogoView.setOnClickListener {
            openUrl("https://x.com/cloffygames")
        }

        // Çıkış butonuna tıklama olayı
        binding.exitButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Geri butonuna tıklama olayı
        binding.backImageButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // Çıkış onay dialogunu gösterir
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.logout_confirmation_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                signOut()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Kullanıcının oturumunu kapatır ve oturum verilerini temizler
    private fun signOut() {
        firebaseAuth.signOut()
        preferencesRepository.clearUserSession()
        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAuthFragment())
    }

    // Tema uygulamasını değiştirir
    private fun applyTheme(darkMode: Boolean) {
        val mode = if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    // Verilen URL'i açar
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    // Görünüm yok edilirken çağrılır
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
