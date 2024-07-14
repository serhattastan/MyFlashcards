package com.cloffygames.myflashcards.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.myflashcards.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Bu anotasyon, Dagger Hilt'in bu ViewModel'e bağımlılık enjeksiyonu yapmasını sağlar
class AuthViewModel @Inject constructor() : ViewModel() {

    // Kullanıcı oturum durumu için LiveData
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // FirebaseAuth örneği
    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        // Kullanıcının oturum durumunu kontrol et
        if (firebaseAuth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // E-posta ve şifre ile giriş yapma
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: R.string.unknown_error.toString())
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: R.string.unknown_error.toString())
            }
        }
    }

    // E-posta ve şifre ile kayıt olma
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: R.string.unknown_error.toString())
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: R.string.unknown_error.toString())
            }
        }
    }

    // Google hesabı ile giriş yapma
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: R.string.unknown_error.toString())
            }
        }
    }

    // Misafir olarak giriş yapma
    fun signInAsGuest() {
        firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: R.string.unknown_error.toString())
            }
        }
    }
}

// Kullanıcı oturum durumunu temsil eden sınıf
sealed class AuthState {
    object Authenticated : AuthState() // Kullanıcı oturum açmış
    object Unauthenticated : AuthState() // Kullanıcı oturum açmamış
    data class Error(val message: String) : AuthState() // Hata durumu
}
