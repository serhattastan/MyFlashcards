package com.cloffygames.myflashcards.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.myflashcards.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel // Bu anotasyon, Dagger Hilt'in bu ViewModel'e bağımlılık enjeksiyonu yapmasını sağlar
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    // Gerekli mantığı uygulayın

    // Bu sınıf, ayarlar ekranıyla ilgili mantığı yönetmek için kullanılabilir
    // preferencesRepository, uygulama tercihlerini yönetmek için kullanılabilir
}