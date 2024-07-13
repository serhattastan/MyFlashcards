package com.cloffygames.myflashcards.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import javax.inject.Singleton

// FirestoreRepository sınıfı, singleton olarak tanımlanmış ve Dagger Hilt tarafından enjekte ediliyor
@Singleton
class FirestoreRepository @Inject constructor() {

    // Firebase Authentication örneği oluşturuluyor
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Email ve şifre ile oturum açma fonksiyonu
    fun signIn(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            // Oturum açma işlemi tamamlandığında callback fonksiyonu ile sonuç dönüyor
            callback(task.isSuccessful)
        }
    }

    // Email ve şifre ile kullanıcı oluşturma fonksiyonu
    fun signUp(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            // Kullanıcı oluşturma işlemi tamamlandığında callback fonksiyonu ile sonuç dönüyor
            callback(task.isSuccessful)
        }
    }

    // Google ile oturum açma fonksiyonu
    fun firebaseAuthWithGoogle(idToken: String, callback: (Boolean) -> Unit) {
        // Google kimlik doğrulama bilgileri oluşturuluyor
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        // Kimlik bilgileri ile oturum açma işlemi gerçekleştiriliyor
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            // Oturum açma işlemi tamamlandığında callback fonksiyonu ile sonuç dönüyor
            callback(task.isSuccessful)
        }
    }
}