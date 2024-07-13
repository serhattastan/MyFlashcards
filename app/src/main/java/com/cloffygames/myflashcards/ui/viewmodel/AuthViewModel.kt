package com.cloffygames.myflashcards.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloffygames.myflashcards.data.repository.FirestoreRepository
import com.cloffygames.myflashcards.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// AuthViewModel sınıfı HiltViewModel olarak tanımlanmış ve Dagger Hilt tarafından enjekte ediliyor
@HiltViewModel
class AuthViewModel @Inject constructor(
    // FirestoreRepository ve PreferencesRepository, Dagger Hilt tarafından enjekte ediliyor
    private val repository: FirestoreRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    // MutableLiveData, dışarıya LiveData olarak açılıyor
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // ViewModel ilk oluşturulduğunda kullanıcı oturumunu kontrol ediyor
    init {
        checkUserSession()
    }

    // Email ve şifre ile oturum açma fonksiyonu
    fun signIn(email: String, password: String) {
        repository.signIn(email, password) { success ->
            if (success) {
                preferencesRepository.saveUserSession() // Oturum durumu kaydediliyor
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error("Sign in failed")
            }
        }
    }

    // Email ve şifre ile kayıt olma fonksiyonu
    fun signUp(email: String, password: String) {
        repository.signUp(email, password) { success ->
            if (success) {
                preferencesRepository.saveUserSession() // Oturum durumu kaydediliyor
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error("Sign up failed")
            }
        }
    }

    // Google ile oturum açma fonksiyonu
    fun firebaseAuthWithGoogle(idToken: String) {
        repository.firebaseAuthWithGoogle(idToken) { success ->
            if (success) {
                preferencesRepository.saveUserSession() // Oturum durumu kaydediliyor
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error("Authentication Failed.")
            }
        }
    }

    // Oturumu kapatma fonksiyonu
    fun signOut() {
        preferencesRepository.clearUserSession() // Oturum durumu temizleniyor
        _authState.value = AuthState.Unauthenticated
    }

    // Kullanıcının oturum açma durumunu kontrol eden fonksiyon
    private fun checkUserSession() {
        if (preferencesRepository.isUserLoggedIn()) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
}

// Oturum durumu için sealed class tanımı
sealed class AuthState {
    object Authenticated : AuthState() // Oturum açılmış durumda
    object Unauthenticated : AuthState() // Oturum açılmamış durumda
    data class Error(val message: String) : AuthState() // Hata durumu ve mesajı
}